package be.llodavid.api.Config;

@Configuration
@ComponentScan(basePackageClasses = Repository.class)
public class RepositoryConfig {
    public class AppConfig {
        @Bean
        public PrinterService‹JsonPrinter› jsonService() {
            return new PrinterService‹JsonPrinter›(new JsonPrinter());
        }
        @Bean
        public PrinterService‹XmlPrinter› xmlService() {
            return new PrinterService‹XmlPrinter›(new XmlPrinter());
        }
    }
}
