package com.electronicarmory.armory;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {

        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.electronicarmory.armory");

        noClasses()
            .that()
                .resideInAnyPackage("com.electronicarmory.armory.service..")
            .or()
                .resideInAnyPackage("com.electronicarmory.armory.repository..")
            .should().dependOnClassesThat()
                .resideInAnyPackage("..com.electronicarmory.armory.web..")
        .because("Services and repositories should not depend on web layer")
        .check(importedClasses);
    }
}
