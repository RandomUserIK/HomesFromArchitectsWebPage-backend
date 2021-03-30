package sk.hfa.projects.domain.enums;

import lombok.Getter;

public enum IndividualSection {

    SAMPLE("sample"),
    DEVELOPER("developer");

    @Getter
    private final String type;

    IndividualSection(String type) {
        this.type = type;
    }

}
