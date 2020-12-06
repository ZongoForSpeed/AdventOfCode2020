package com.adventofcode;

import com.adventofcode.utils.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class Day04Test {
    private static final Set<String> MANDATORY_FIELDS =
            Set.of("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid");

    public static List<List<String>> readBatchFile(List<String> batch) {
        List<List<String>> passports = new ArrayList<>();
        List<String> currentPassport = new ArrayList<>();

        for (String line : batch) {
            if (line.isEmpty()) {
                passports.add(currentPassport);
                currentPassport = new ArrayList<>();
            } else {
                String[] split = line.split(" ");
                currentPassport.addAll(Arrays.asList(split));
            }
        }

        passports.add(currentPassport);
        return passports;
    }

    public static boolean checkPassportFields(Map<String, String> fields) {
        for (PassportFields passportFields : PassportFields.values()) {
            String value = fields.get(passportFields.name());
            if (value == null && !passportFields.isOptional()) {
                return false;
            }
            if (!passportFields.validField(value)) {
                return false;
            }
        }
        return true;
    }

    public static long passportProcessing(List<String> batch) {
        List<List<String>> passports = readBatchFile(batch);
        return passports.stream()
                .map(Day04Test::readPassport)
                .filter(Day04Test::checkMandatoryFields)
                .count();
    }

    private static Map<String, String> readPassport(List<String> passport) {
        return passport.stream().map(s -> s.split(":")).collect(Collectors.toMap(s -> s[0], s -> s[1]));
    }

    private static boolean checkMandatoryFields(Map<String, String> fields) {
        return MANDATORY_FIELDS.stream().allMatch(fields::containsKey);
    }

    @Test
    void testPassportProcessing() {
        List<String> batch = List.of(
                "ecl:gry pid:860033327 eyr:2020 hcl:#fffffd",
                "byr:1937 iyr:2017 cid:147 hgt:183cm",
                "",
                "iyr:2013 ecl:amb cid:350 eyr:2023 pid:028048884",
                "hcl:#cfa07d byr:1929",
                "",
                "hcl:#ae17e1 iyr:2013",
                "eyr:2024",
                "ecl:brn pid:760753108 byr:1931",
                "hgt:179cm",
                "",
                "hcl:#cfa07d eyr:2025 pid:166559648",
                "iyr:2011 ecl:brn hgt:59in"
        );

        assertThat(passportProcessing(batch)).isEqualTo(2);
    }

    @Test
    void testInvalidPassports() {
        List<String> batch = List.of("eyr:1972 cid:100",
                "hcl:#18171d ecl:amb hgt:170 pid:186cm iyr:2018 byr:1926",
                "",
                "iyr:2019",
                "hcl:#602927 eyr:1967 hgt:170cm",
                "ecl:grn pid:012533040 byr:1946",
                "",
                "hcl:dab227 iyr:2012",
                "ecl:brn hgt:182cm pid:021572410 eyr:2020 byr:1992 cid:277",
                "",
                "hgt:59cm ecl:zzz",
                "eyr:2038 hcl:74454a iyr:2023",
                "pid:3556412378 byr:2007");

        assertThat(readBatchFile(batch).stream()
                .map(Day04Test::readPassport)
                .noneMatch(p -> checkMandatoryFields(p) && checkPassportFields(p)))
                .isTrue();
    }

    @Test
    void testValidPassports() {
        List<String> batch = List.of("pid:087499704 hgt:74in ecl:grn iyr:2012 eyr:2030 byr:1980",
                "hcl:#623a2f",
                "",
                "eyr:2029 ecl:blu cid:129 byr:1989",
                "iyr:2014 pid:896056539 hcl:#a97842 hgt:165cm",
                "",
                "hcl:#888785",
                "hgt:164cm byr:2001 iyr:2015 cid:88",
                "pid:545766238 ecl:hzl",
                "eyr:2022",
                "",
                "iyr:2010 hgt:158cm hcl:#b6652a ecl:blu byr:1944 eyr:2021 pid:093154719");

        assertThat(readBatchFile(batch).stream()
                .map(Day04Test::readPassport)
                .allMatch(p -> checkMandatoryFields(p) && checkPassportFields(p)))
                .isTrue();
    }

    @Test
    void testPassportFields() {
        assertThat(PassportFields.byr.validField("2002")).isTrue();
        assertThat(PassportFields.byr.validField("2003")).isFalse();

        assertThat(PassportFields.hgt.validField("60in")).isTrue();
        assertThat(PassportFields.hgt.validField("190cm")).isTrue();
        assertThat(PassportFields.hgt.validField("190in")).isFalse();
        assertThat(PassportFields.hgt.validField("190")).isFalse();

        assertThat(PassportFields.hcl.validField("#123abc")).isTrue();
        assertThat(PassportFields.hcl.validField("#123abz")).isFalse();
        assertThat(PassportFields.hcl.validField("123abc")).isFalse();

        assertThat(PassportFields.ecl.validField("brn")).isTrue();
        assertThat(PassportFields.ecl.validField("wat")).isFalse();

        assertThat(PassportFields.pid.validField("000000001")).isTrue();
        assertThat(PassportFields.pid.validField("0123456789")).isFalse();

    }

    /**
     * --- Day 4: Passport Processing ---
     * You arrive at the airport only to realize that you grabbed your North Pole
     * Credentials instead of your passport. While these documents are extremely
     * similar, North Pole Credentials aren't issued by a country and therefore
     * aren't actually valid documentation for travel in most of the world.
     * <p>
     * It seems like you're not the only one having problems, though; a very long
     * line has formed for the automatic passport scanners, and the delay could
     * upset your travel itinerary.
     * <p>
     * Due to some questionable network security, you realize you might be able to
     * solve both of these problems at the same time.
     * <p>
     * The automatic passport scanners are slow because they're having trouble
     * detecting which passports have all required fields. The expected fields are
     * as follows:
     * <p>
     * byr (Birth Year)
     * iyr (Issue Year)
     * eyr (Expiration Year)
     * hgt (Height)
     * hcl (Hair Color)
     * ecl (Eye Color)
     * pid (Passport ID)
     * cid (Country ID)
     * <p>
     * Passport data is validated in batch files (your puzzle input). Each
     * passport is represented as a sequence of key:value pairs separated by
     * spaces or newlines. Passports are separated by blank lines.
     * <p>
     * Here is an example batch file containing four passports:
     * <p>
     * ecl:gry pid:860033327 eyr:2020 hcl:#fffffd
     * byr:1937 iyr:2017 cid:147 hgt:183cm
     * <p>
     * iyr:2013 ecl:amb cid:350 eyr:2023 pid:028048884
     * hcl:#cfa07d byr:1929
     * <p>
     * hcl:#ae17e1 iyr:2013
     * eyr:2024
     * ecl:brn pid:760753108 byr:1931
     * hgt:179cm
     * <p>
     * hcl:#cfa07d eyr:2025 pid:166559648
     * iyr:2011 ecl:brn hgt:59in
     * <p>
     * The first passport is valid - all eight fields are present. The second passport
     * is invalid - it is missing hgt (the Height field).
     * <p>
     * The third passport is interesting; the only missing field is cid, so it
     * looks like data from North Pole Credentials, not a passport at all! Surely,
     * nobody would mind if you made the system temporarily ignore missing cid
     * fields. Treat this "passport" as valid.
     * <p>
     * The fourth passport is missing two fields, cid and byr. Missing cid is
     * fine, but missing any other field is not, so this passport is invalid.
     * <p>
     * According to the above rules, your improved system would report 2 valid
     * passports.
     * <p>
     * Count the number of valid passports - those that have all required fields.
     * Treat cid as optional. In your batch file, how many passports are valid?
     * <p>
     * --- Part Two ---
     * The line is moving more quickly now, but you overhear airport security
     * talking about how passports with invalid data are getting through. Better
     * add some data validation, quick!
     * <p>
     * You can continue to ignore the cid field, but each other field has strict
     * rules about what values are valid for automatic validation:
     * <p>
     * byr (Birth Year) - four digits; at least 1920 and at most 2002.
     * iyr (Issue Year) - four digits; at least 2010 and at most 2020.
     * eyr (Expiration Year) - four digits; at least 2020 and at most 2030.
     * hgt (Height) - a number followed by either cm or in:
     * If cm, the number must be at least 150 and at most 193.
     * If in, the number must be at least 59 and at most 76.
     * hcl (Hair Color) - a # followed by exactly six characters 0-9 or a-f.
     * ecl (Eye Color) - exactly one of: amb blu brn gry grn hzl oth.
     * pid (Passport ID) - a nine-digit number, including leading zeroes.
     * cid (Country ID) - ignored, missing or not.
     * <p>
     * Your job is to count the passports where all required fields are both
     * present and valid according to the above rules. Here are some example
     * values:
     * <p>
     * byr valid:   2002
     * byr invalid: 2003
     * <p>
     * hgt valid:   60in
     * hgt valid:   190cm
     * hgt invalid: 190in
     * hgt invalid: 190
     * <p>
     * hcl valid:   #123abc
     * hcl invalid: #123abz
     * hcl invalid: 123abc
     * <p>
     * ecl valid:   brn
     * ecl invalid: wat
     * <p>
     * pid valid:   000000001
     * pid invalid: 0123456789
     * Here are some invalid passports:
     * <p>
     * eyr:1972 cid:100
     * hcl:#18171d ecl:amb hgt:170 pid:186cm iyr:2018 byr:1926
     * <p>
     * iyr:2019
     * hcl:#602927 eyr:1967 hgt:170cm
     * ecl:grn pid:012533040 byr:1946
     * <p>
     * hcl:dab227 iyr:2012
     * ecl:brn hgt:182cm pid:021572410 eyr:2020 byr:1992 cid:277
     * <p>
     * hgt:59cm ecl:zzz
     * eyr:2038 hcl:74454a iyr:2023
     * pid:3556412378 byr:2007
     * Here are some valid passports:
     * <p>
     * pid:087499704 hgt:74in ecl:grn iyr:2012 eyr:2030 byr:1980
     * hcl:#623a2f
     * <p>
     * eyr:2029 ecl:blu cid:129 byr:1989
     * iyr:2014 pid:896056539 hcl:#a97842 hgt:165cm
     * <p>
     * hcl:#888785
     * hgt:164cm byr:2001 iyr:2015 cid:88
     * pid:545766238 ecl:hzl
     * eyr:2022
     * <p>
     * iyr:2010 hgt:158cm hcl:#b6652a ecl:blu byr:1944 eyr:2021 pid:093154719
     * Count the number of valid passports - those that have all required fields and valid values. Continue to treat cid as optional. In your batch file, how many passports are valid?
     */
    @Test
    void inputPassportProcessing() throws IOException {
        List<String> batch = FileUtils.readLines("/day/4/input");

        assertThat(passportProcessing(batch)).isEqualTo(210);


        assertThat(readBatchFile(batch).stream()
                .map(Day04Test::readPassport)
                .filter(p -> checkMandatoryFields(p) && checkPassportFields(p))
                .count())
                .isEqualTo(131);
    }

    enum PassportFields {
        byr(false) {
            // (Birth Year) - four digits; at least 1920 and at most 2002.
            @Override
            boolean validField(String value) {
                int year = Integer.parseInt(value);
                return value.length() == 4 && year >= 1920 && year <= 2002;
            }
        },
        iyr(false) {
            // (Issue Year) - four digits; at least 2010 and at most 2020.
            @Override
            boolean validField(String value) {
                int year = Integer.parseInt(value);
                return value.length() == 4 && year >= 2010 && year <= 2020;
            }
        },
        eyr(false) {
            // (Expiration Year) - four digits; at least 2020 and at most 2030.
            @Override
            boolean validField(String value) {
                int year = Integer.parseInt(value);
                return value.length() == 4 && year >= 2020 && year <= 2030;
            }
        },
        hgt(false) {
            // (Height) - a number followed by either cm or in:
            // If cm, the number must be at least 150 and at most 193.
            // If in, the number must be at least 59 and at most 76.
            @Override
            boolean validField(String value) {
                Pattern compile = Pattern.compile("(\\d+)(in|cm)");
                Matcher matcher = compile.matcher(value);
                if (matcher.find()) {
                    int height = Integer.parseInt(matcher.group(1));

                    switch (matcher.group(2)) {
                        case "in":
                            return height >= 59 && height <= 76;
                        case "cm":
                            return height >= 150 && height <= 193;
                        default:
                            return false;
                    }
                }

                return false;
            }
        },
        hcl(false) {
            public final Pattern PATTERN = Pattern.compile("#[0-9a-f]{6}");

            // (Hair Color) - a # followed by exactly six characters 0-9 or a-f.
            @Override
            boolean validField(String value) {
                return PATTERN.matcher(value).find();
            }
        },
        ecl(false) {
            private final Set<String> EYE_COLORS = Set.of("amb", "blu", "brn", "gry", "grn", "hzl", "oth");

            // (Eye Color) - exactly one of: amb blu brn gry grn hzl oth.
            @Override
            boolean validField(String value) {
                return EYE_COLORS.contains(value);
            }
        },
        pid(false) {
            public final Pattern PATTERN = Pattern.compile("[0-9]{9}");

            // (Passport ID) - a nine-digit number, including leading zeroes.
            @Override
            boolean validField(String value) {
                return value.length() == 9 && PATTERN.matcher(value).find();
            }
        },
        cid(true) {
            // (Country ID) - ignored, missing or not.
            @Override
            boolean validField(String value) {
                return true;
            }
        };


        private final boolean optional;

        PassportFields(boolean optional) {
            this.optional = optional;
        }

        abstract boolean validField(String value);

        public boolean isOptional() {
            return optional;
        }
    }
}
