package ma.ensa.springbatch.config;

import ma.ensa.springbatch.batch.BatchLauncher;
import ma.ensa.springbatch.entities.Transaction;
import ma.ensa.springbatch.entities.TransactionContainer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableBatchProcessing//catte annotation permet automatiquement la creaction de certains object(beans) en mémpoire pour les injecter facilement dans notre application
@EnableScheduling
public class SpringBatchConfig {
    // pour créer un Job nous avous besion des aubjects suivant :

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private ItemReader<TransactionContainer> transactionContainerItemReader;
    @Autowired
    private ItemProcessor<TransactionContainer, Transaction> transactionItemProcessor;
    @Autowired
    private ItemWriter<Transaction> transactionItemWriter;


    /*      La créeation des Beans ItemReader   (implementation des méthodes)   */
    @Bean
    public FlatFileItemReader<TransactionContainer> fileItemReader(
            @Value("${inputFile}") Resource resource){
        FlatFileItemReader<TransactionContainer> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setName("Data-CSV-READER");
        flatFileItemReader.setLinesToSkip(1);// permet de sauter la 1ier ligne du fichier qui contient just l'entete.
        flatFileItemReader.setResource(resource); //la resource qui est le fichier à traiter
        flatFileItemReader.setLineMapper(lineMapper()); // c'est une méthode qu'on configure en bas
        return  flatFileItemReader;
    }

    @Bean
    public LineMapper<TransactionContainer> lineMapper() {
        DefaultLineMapper<TransactionContainer> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");// on féfinit le séparateur dans notre fichier data
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("idTransaction", "idCompte", "montant", "dateTransaction");// on féfinie l'ordre des données dans le fichier en utilisant les noms des parametres dans la classe
        lineMapper.setLineTokenizer(lineTokenizer);
        BeanWrapperFieldSetMapper<TransactionContainer> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(TransactionContainer.class);// on spécifie le type sible
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;
    }
    /*  ---------------------------------------------------------------------------------------     */

    /* Création d'un Job avec son Step */
    @Bean
    public Job bankJob() {
        Step step1 = stepBuilderFactory.get("step-data-load")
                .<TransactionContainer, Transaction>chunk(10)//chunk est une méthode générique qui a besoin de spécifier de types Input et Output
                .reader(transactionContainerItemReader)
                .processor(transactionItemProcessor) //on cas d'un seul Itemprocessor dans l'app
                .writer(transactionItemWriter)
                .build();
        return jobBuilderFactory.get("data-loder-job")
                .start(step1)
                .build();
    }
    /*  -------------------------------------------------------------   */
//    @Autowired
//    private JobRepository jobRepository;
//
//    @Bean(name="jobLauncher")
//    public JobLauncher jobLauncher() throws Exception {
//        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
//        jobLauncher.setJobRepository(jobRepository);
//        return jobLauncher;
//    }

    @Bean
    public BatchLauncher launchBatch(){
        return new BatchLauncher();
    }

    @Scheduled(cron = "0 00 05 1 * ?")
    public void scheduleFixedDelayTask() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
        System.out.println(launchBatch().load());
    }

}
