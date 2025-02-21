/*
 * Created on 30 Nov 2023
 *
 * author dimitry
 */
package org.freeplane.plugin.codeexplorer.task;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Test;

public class CodeExplorerConfigurationsTest {

    private static File CONFIGURATION_FILE;
    static {
        try {
            CONFIGURATION_FILE = File.createTempFile("configurationFile", ".json");
        } catch (IOException e) {/**/}
    }

    private static void serialize(CodeExplorerConfiguration singleConfiguration) {
        singleConfiguration.initialize();
        new CodeExplorerConfigurations(Collections.singletonList(singleConfiguration)).saveConfiguration(CONFIGURATION_FILE);
    }

    private static CodeExplorerConfiguration deserialize() {
        return CodeExplorerConfigurations.loadConfigurations(CONFIGURATION_FILE).getConfigurations().get(0);
    }

    @After
    public void deleteTestFile() {
        CONFIGURATION_FILE.delete();
    }

    @Test
    public void createsConfigurationFromEmptyString() throws Exception {
        Assertions.assertThat(CodeExplorerConfigurations.loadConfigurations(new File("unknown.json")).getConfigurations()).isEmpty();
    }

    @Test
    public void serializesAndDeserializesEmptyConfigurations() throws Exception {
        CodeExplorerConfiguration uut = new CodeExplorerConfiguration("", Collections.emptyList(), "");
        serialize(uut);
        Assertions.assertThat(deserialize())
        .usingRecursiveComparison()
        .isEqualTo(uut);
    }

    @Test
    public void serializesAndDeserializesConfigurationContainingProjectName() throws Exception {
        CodeExplorerConfiguration uut = new CodeExplorerConfiguration("project name", Collections.emptyList(), "");
        serialize(uut);
        Assertions.assertThat(deserialize())
        .usingRecursiveComparison()
        .isEqualTo(uut);
    }

    @Test
    public void serializesAndDeserializesConfigurationContainingProjectNameWithTab() throws Exception {
        CodeExplorerConfiguration uut = new CodeExplorerConfiguration("project\tname", Collections.emptyList(), "");
        serialize(uut);
        Assertions.assertThat(deserialize())
        .usingRecursiveComparison()
        .isEqualTo(uut);
    }

    @Test
    public void serializesAndDeserializesConfigurationContainingRules() throws Exception {
        CodeExplorerConfiguration uut = new CodeExplorerConfiguration("", Collections.emptyList(), " a ->^ b\n b ->v c");
        serialize(uut);
        Assertions.assertThat(deserialize())
        .usingRecursiveComparison()
        .isEqualTo(uut);
    }

    @Test
    public void serializesAndDeserializesConfigurationContainingProjectNameAndRules() throws Exception {
        CodeExplorerConfiguration uut = new CodeExplorerConfiguration("project name", Collections.emptyList(), " a ->^ b\n b ->v c");
        serialize(uut);
        Assertions.assertThat(deserialize())
        .usingRecursiveComparison()
        .isEqualTo(uut);
    }

    @Test
    public void serializesAndDeserializesConfigurationContainingLocations() throws Exception {
        CodeExplorerConfiguration uut = new CodeExplorerConfiguration("",
                Arrays.asList(new File("a"), new File("b")), "");
        serialize(uut);
        Assertions.assertThat(deserialize())
        .usingRecursiveComparison()
        .isEqualTo(uut);
    }

    @Test
    public void serializesAndDeserializesConfigurationContainingLocationsAndRules() throws Exception {
        CodeExplorerConfiguration uut = new CodeExplorerConfiguration("",
                Arrays.asList(new File("a"), new File("b")), "");
        serialize(uut);
        Assertions.assertThat(deserialize())
        .usingRecursiveComparison()
        .isEqualTo(uut);
    }

}
