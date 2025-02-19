package io.github.aleksandarharalanov.chatguard.core.security.captcha;

import io.github.aleksandarharalanov.chatguard.core.data.CaptchaData;

import java.util.Random;

public final class CaptchaGenerator {

    private CaptchaGenerator() {}

    public static String generateCaptchaCode() {
        StringBuilder captcha = new StringBuilder(CaptchaData.getCodeLength());
        Random random = new Random();

        for (int i = 0; i < CaptchaData.getCodeLength(); i++) {
            captcha.append(CaptchaData.getCodeCharacters()
                    .charAt(random.nextInt(CaptchaData.getCodeCharacters().length())));
        }

        return captcha.toString();
    }
}
