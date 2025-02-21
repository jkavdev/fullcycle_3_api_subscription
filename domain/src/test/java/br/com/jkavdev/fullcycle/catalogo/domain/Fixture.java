package br.com.jkavdev.fullcycle.catalogo.domain;

import br.com.jkavdev.fullcycle.catalogo.domain.castmember.CastMember;
import br.com.jkavdev.fullcycle.catalogo.domain.castmember.CastMemberType;
import br.com.jkavdev.fullcycle.catalogo.domain.category.Category;
import br.com.jkavdev.fullcycle.catalogo.domain.genre.Genre;
import br.com.jkavdev.fullcycle.catalogo.domain.utils.IdUtils;
import br.com.jkavdev.fullcycle.catalogo.domain.utils.InstantUtils;
import br.com.jkavdev.fullcycle.catalogo.domain.video.Rating;
import br.com.jkavdev.fullcycle.catalogo.domain.video.Video;
import net.datafaker.Faker;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class Fixture {

    private static final Faker FAKER = new Faker();

    public static String name() {
        return FAKER.name().fullName();
    }

    public static Integer year() {
        return FAKER.random().nextInt(2020, 2030);
    }

    public static Double duration() {
        return FAKER.options().option(120.0, 15.5, 35.5, 10.0, 2.0);
    }

    public static boolean bool() {
        return FAKER.bool().bool();
    }

    public static String title() {
        return FAKER.options().option(
                "System Design no Mercado Livre na prática",
                "Não cometa esses erros ao trabalhar com Microsserviços",
                "Testes de Mutação. Você não testa seu software corretamente"
        );
    }

    public static String checksum() {
        return "03fe62de";
    }

    public static final class Categories {

        public static Category aulas() {
            return Category.with(
                    UUID.randomUUID().toString().replace("-", ""),
                    "Aulas",
                    "Conteudo gravado",
                    true,
                    InstantUtils.now(),
                    InstantUtils.now(),
                    null
            );
        }

        public static Category lives() {
            return Category.with(
                    UUID.randomUUID().toString().replace("-", ""),
                    "Lives",
                    "Conteudo ao vivo",
                    true,
                    InstantUtils.now(),
                    InstantUtils.now(),
                    null
            );
        }

        public static Category talks() {
            return Category.with(
                    UUID.randomUUID().toString().replace("-", ""),
                    "Talks",
                    "Conteudo ao vivo",
                    false,
                    InstantUtils.now(),
                    InstantUtils.now(),
                    InstantUtils.now()
            );
        }

    }

    public static final class CastMembers {

        private static final CastMember WESLEY =
                CastMember.with(
                        UUID.randomUUID().toString(),
                        "Wesley FullCycle",
                        CastMemberType.ACTOR,
                        InstantUtils.now(),
                        InstantUtils.now()
                );

        private static final CastMember GABRIEL =
                CastMember.with(
                        UUID.randomUUID().toString(),
                        "Gabriel FullCycle",
                        CastMemberType.ACTOR,
                        InstantUtils.now(),
                        InstantUtils.now()
                );

        private static final CastMember LEONAN =
                CastMember.with(
                        UUID.randomUUID().toString(),
                        "Leonan FullCycle",
                        CastMemberType.DIRECTOR,
                        InstantUtils.now(),
                        InstantUtils.now()
                );

        public static CastMemberType type() {
            return FAKER.options().option(CastMemberType.values());
        }

        // criando os cast members novamente, devido os cast members estaticos estarem muito perto, entao a data do instant utils
        // praticamente a mesma, baguncando os testes unitarios
        public static CastMember wesley() {
            return CastMember.with(
                    UUID.randomUUID().toString(),
                    "Wesley FullCycle",
                    CastMemberType.ACTOR,
                    InstantUtils.now(),
                    InstantUtils.now()
            );
        }

        public static CastMember gabriel() {
            return CastMember.with(
                    UUID.randomUUID().toString(),
                    "Gabriel FullCycle",
                    CastMemberType.ACTOR,
                    InstantUtils.now(),
                    InstantUtils.now()
            );
        }

        public static CastMember leonan() {
            return CastMember.with(
                    UUID.randomUUID().toString(),
                    "Leonan FullCycle",
                    CastMemberType.DIRECTOR,
                    InstantUtils.now(),
                    InstantUtils.now()
            );
        }
    }

    public static final class Genres {

        public static Genre tech() {
            return Genre.with(
                    IdUtils.uniqueId(),
                    "Technology",
                    true,
                    Set.of("c1"),
                    InstantUtils.now(),
                    InstantUtils.now(),
                    null
            );
        }

        public static Genre business() {
            return Genre.with(
                    IdUtils.uniqueId(),
                    "Business",
                    false,
                    new HashSet<>(),
                    InstantUtils.now(),
                    InstantUtils.now(),
                    InstantUtils.now()
            );
        }

        public static Genre marketing() {
            return Genre.with(
                    IdUtils.uniqueId(),
                    "Marketing",
                    true,
                    Set.of("c2"),
                    InstantUtils.now(),
                    InstantUtils.now(),
                    null
            );
        }
    }

    public static final class Videos {
        public static Rating rating() {
            return FAKER.options().option(Rating.values());
        }

        public static Video systemDesign() {
            return Video.with(
                    IdUtils.uniqueId(),
                    "System Design no Mercado Livre na prática",
                    "O vídeo mais assistido",
                    2022,
                    Fixture.duration(),
                    Rating.AGE_16.getName(),
                    true,
                    true,
                    InstantUtils.now().toString(),
                    InstantUtils.now().toString(),
                    "http://video",
                    "http://trailer",
                    "http://banner",
                    "http://thumb",
                    "http://thumbhalf",
                    Set.of("aulas"),
                    Set.of("luiz"),
                    Set.of("systemdesign")
            );
        }

        public static Video java21() {
            return Video.with(
                    IdUtils.uniqueId(),
                    "Java 21",
                    "Java FTW",
                    2023,
                    Fixture.duration(),
                    Rating.AGE_10.getName(),
                    true,
                    true,
                    InstantUtils.now().toString(),
                    InstantUtils.now().toString(),
                    "http://video",
                    "http://trailer",
                    "http://banner",
                    "http://thumb",
                    "http://thumbhalf",
                    Set.of("lives"),
                    Set.of("gabriel"),
                    Set.of("java")
            );
        }

        public static Video golang() {
            return Video.with(
                    IdUtils.uniqueId(),
                    "Golang 1.22",
                    "Um vídeo da linguagem go",
                    2024,
                    Fixture.duration(),
                    Rating.L.getName(),
                    true,
                    true,
                    InstantUtils.now().toString(),
                    InstantUtils.now().toString(),
                    "http://video",
                    "http://trailer",
                    "http://banner",
                    "http://thumb",
                    "http://thumbhalf",
                    Set.of("meeting"),
                    Set.of("wesley"),
                    Set.of("golang")
            );
        }
    }

}
