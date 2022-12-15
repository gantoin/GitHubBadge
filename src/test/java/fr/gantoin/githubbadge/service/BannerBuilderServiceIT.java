package fr.gantoin.githubbadge.service;

import java.io.File;
import java.util.Objects;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BannerBuilderServiceIT {

    private final BannerBuilderService bannerBuilderService = new BannerBuilderService();

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static void cleanTmp() {
        // remove all files from tmp
        File tmp = new File("/tmp");
        for (File file : Objects.requireNonNull(tmp.listFiles())) {
            file.delete();
        }
    }

    @BeforeEach
    void setUp() {
        cleanTmp();
    }

    @AfterEach
    void tearDown() {
        cleanTmp();
    }

    @Test
    void buildBanner() {
        bannerBuilderService.buildBanner("Antoine");
        Assertions.assertThat(new File("/tmp/result.png")).exists();
    }
}
