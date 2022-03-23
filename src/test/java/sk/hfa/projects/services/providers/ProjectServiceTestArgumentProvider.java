package sk.hfa.projects.services.providers;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

public class ProjectServiceTestArgumentProvider implements ArgumentsProvider {

    @Override
    public Stream<Arguments> provideArguments(ExtensionContext extensionContext) {
        return Stream.of(
                ProjectRequest_ProjectEntity.getIndividual(),
                ProjectRequest_ProjectEntity.getInterior(),
                ProjectRequest_ProjectEntity.getCommon()).map(Arguments::of);
    }

}
