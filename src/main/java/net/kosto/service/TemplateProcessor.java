/*
 * Copyright 2018 Kostyantyn Krakovych
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.kosto.service;

import freemarker.template.Configuration;
import net.kosto.Package;

import static freemarker.template.Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS;
import static freemarker.template.Configuration.VERSION_2_3_28;
import static freemarker.template.TemplateExceptionHandler.RETHROW_HANDLER;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Locale.US;
import static net.kosto.util.FileUtils.UNIX_SEPARATOR;

public class TemplateProcessor {

    private static TemplateProcessor instance;

    private final Configuration configuration;

    private TemplateProcessor() {
        configuration = new Configuration(DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        configuration.setClassForTemplateLoading(Package.class, UNIX_SEPARATOR);
        configuration.setIncompatibleImprovements(VERSION_2_3_28);
        configuration.setDefaultEncoding(UTF_8.name());
        configuration.setLocale(US);
        configuration.setTemplateExceptionHandler(RETHROW_HANDLER);
    }

    public static synchronized TemplateProcessor getInstance() {
        if (instance == null) {
            instance = new TemplateProcessor();
        }
        return instance;
    }

    public Configuration getConfiguration() {
        return configuration;
    }
}
