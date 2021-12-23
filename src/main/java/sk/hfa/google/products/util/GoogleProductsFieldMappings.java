package sk.hfa.google.products.util;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class GoogleProductsFieldMappings {

    public static final Map<String, String> DEFAULT_PROJECT_FIELD_MAPPINGS = ImmutableMap.<String, String>builder()
            .put("builtUpArea", "Zastavaná plocha")
            .put("persons", "Počet osôb")
            .put("usableArea", "Celková plocha")
            .put("hasGarage", "Obsahuje projekt garáž?")
            .put("energeticClass", "Energetická trieda projektu")
            .build();

    public static final Map<String, String> COMMON_PROJECT_FIELD_MAPPINGS = ImmutableMap.<String, String>builder()
            .put("rooms", "Počet izieb")
            .put("totalLivingArea", "Úžitková plocha")
            .put("selfHelpBuildPrice", "Svojpomocná výstavba")
            .put("onKeyPrice", "Cena na kľúč")
            .put("basicProjectPrice", "Cena základného projektu")
            .put("extendedProjectPrice", "Cena rozšíreného projektu")
            .put("roofPitch", "Sklon strechy")
            .put("minimumParcelWidth", "Minimálnu šírku pozemku")
            .put("heatingSource", "Zdroj vykurovania")
            .put("heatingType", "Typ vykurovania")
            .put("hasStorey", "Je poschodový?")
            .put("entryOrientation", "Orientácia projektu")
            .build();

    private GoogleProductsFieldMappings() {
    }

}
