package br.com.jkavdev.fullcycle.catalogo;

import br.com.jkavdev.fullcycle.catalogo.infrastructure.configuration.ObjectMapperConfig;
import org.junit.jupiter.api.Tag;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.annotation.AliasFor;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ActiveProfiles("test-integration")
@GraphQlTest(
        // removendo a classe de configuracao de object mapper, pois tava interferindo, porque o padrao de ObjectMapperConfig
        // snake case, e precisamos do padrao que eh camel case para os endpoints de graphql
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = ObjectMapperConfig.class)
)
@Tag("integrationTests")
public @interface GraphQLControllerTest {

    @AliasFor(annotation = GraphQlTest.class, attribute = "controllers")
    Class<?>[] controllers() default {};
}
