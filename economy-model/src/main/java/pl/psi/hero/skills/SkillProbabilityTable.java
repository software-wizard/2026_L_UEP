package pl.psi.hero.skills;

import pl.psi.hero.HeroClass;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SkillProbabilityTable {
    private static final String RESOURCE_PATH = "/skills/secondary_skill_weights.csv";
    private static final SkillProbabilityTable DEFAULT = loadDefault();

    private final Map<SkillName, Map<HeroClass, Integer>> weights;

    public SkillProbabilityTable(Reader reader) {
        this.weights = parse(reader);
    }

    public static SkillProbabilityTable getDefault() {
        return DEFAULT;
    }

    public int getWeight(HeroClass heroClass, SkillName skillName) {
        Map<HeroClass, Integer> skillWeights = weights.get(skillName);
        if (skillWeights == null) {
            throw new IllegalArgumentException("Missing skill weights for " + skillName);
        }
        Integer weight = skillWeights.get(heroClass);
        if (weight == null) {
            throw new IllegalArgumentException("Missing " + heroClass + " weight for " + skillName);
        }
        return weight;
    }

    private static SkillProbabilityTable loadDefault() {
        InputStream inputStream = SkillProbabilityTable.class.getResourceAsStream(RESOURCE_PATH);
        if (inputStream == null) {
            throw new IllegalStateException("Missing skill probability table resource: " + RESOURCE_PATH);
        }
        return new SkillProbabilityTable(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
    }

    private Map<SkillName, Map<HeroClass, Integer>> parse(Reader reader) {
        try (BufferedReader bufferedReader = new BufferedReader(reader)) {
            String header = bufferedReader.readLine();
            if (header == null) {
                throw new IllegalArgumentException("Skill probability table is empty.");
            }

            String[] columns = header.split(",", -1);
            validateHeader(columns);

            Map<SkillName, Map<HeroClass, Integer>> parsedWeights = new EnumMap<>(SkillName.class);
            String line;
            int lineNumber = 1;
            while ((line = bufferedReader.readLine()) != null) {
                lineNumber++;
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] values = line.split(",", -1);
                if (values.length != columns.length) {
                    throw new IllegalArgumentException("Invalid column count at line " + lineNumber);
                }

                SkillName skillName = parseSkillName(values[0], lineNumber);
                if (parsedWeights.containsKey(skillName)) {
                    throw new IllegalArgumentException("Duplicate skill row: " + skillName);
                }

                Map<HeroClass, Integer> classWeights = new EnumMap<>(HeroClass.class);
                for (int i = 1; i < columns.length; i++) {
                    HeroClass heroClass = HeroClass.valueOf(columns[i]);
                    classWeights.put(heroClass, parseWeight(values[i], skillName, heroClass, lineNumber));
                }
                parsedWeights.put(skillName, classWeights);
            }

            validateAllSkillsPresent(parsedWeights);
            return parsedWeights;
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read skill probability table.", e);
        }
    }

    private void validateHeader(String[] columns) {
        if (columns.length != HeroClass.values().length + 1) {
            throw new IllegalArgumentException("Skill probability table must contain skill column and all hero classes.");
        }
        if (!"skill".equals(columns[0])) {
            throw new IllegalArgumentException("First column must be skill.");
        }

        Set<HeroClass> heroClasses = new HashSet<>();
        for (int i = 1; i < columns.length; i++) {
            if (columns[i].isBlank()) {
                throw new IllegalArgumentException("Missing hero class in header.");
            }
            heroClasses.add(HeroClass.valueOf(columns[i]));
        }
        for (HeroClass heroClass : HeroClass.values()) {
            if (!heroClasses.contains(heroClass)) {
                throw new IllegalArgumentException("Missing hero class column: " + heroClass);
            }
        }
    }

    private SkillName parseSkillName(String value, int lineNumber) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Missing skill name at line " + lineNumber);
        }
        return SkillName.valueOf(value);
    }

    private int parseWeight(String value, SkillName skillName, HeroClass heroClass, int lineNumber) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Missing weight for " + skillName + " and " + heroClass + " at line " + lineNumber);
        }
        try {
            int weight = Integer.parseInt(value);
            if (weight < 0) {
                throw new IllegalArgumentException("Negative weight for " + skillName + " and " + heroClass + " at line " + lineNumber);
            }
            return weight;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid weight for " + skillName + " and " + heroClass + " at line " + lineNumber, e);
        }
    }

    private void validateAllSkillsPresent(Map<SkillName, Map<HeroClass, Integer>> parsedWeights) {
        for (SkillName skillName : SkillName.values()) {
            if (!parsedWeights.containsKey(skillName)) {
                throw new IllegalArgumentException("Missing skill row: " + skillName);
            }
        }
    }
}
