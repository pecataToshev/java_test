package de.lathspell.test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import lombok.extern.slf4j.Slf4j;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.PlatformTransactionManager;
import static org.springframework.transaction.TransactionDefinition.ISOLATION_READ_COMMITTED;
import static org.springframework.transaction.TransactionDefinition.PROPAGATION_SUPPORTS;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import de.lathspell.test.config.AppConfiguration;

@RunWith(SpringRunner.class)
@ActiveProfiles("postgres") // "h2" or "postgres"
@ContextConfiguration(classes = AppConfiguration.class)
@Slf4j
public class SpringTransactionsTest {

    @Autowired
    private PlatformTransactionManager springPlatformTransactionManager;

    @Autowired
    private EntityManagerFactory emf;

    /**
     * FIXME: Does not seem to use any transactions at all.
     */
    @Test
    public void testSpring1() {
        TransactionTemplate tt = new TransactionTemplate(springPlatformTransactionManager);
        String result = tt.execute((status) -> {
            EntityManager em = emf.createEntityManager();
            return (String) em.createNativeQuery("SELECT cast('foo' as varchar)").getSingleResult();
        });
        assertEquals("foo", result);
    }

    /**
     * FIXME: Does not seem to use any transactions at all.
     */
    @Test
    public void testSpring2() throws Exception {
        DefaultTransactionDefinition txDef = new DefaultTransactionDefinition();
        txDef.setIsolationLevel(ISOLATION_READ_COMMITTED);
        txDef.setPropagationBehavior(PROPAGATION_SUPPORTS); // No Tx?
        TransactionStatus status = springPlatformTransactionManager.getTransaction(txDef);
        log.info("Using TxStatus: " + status);
        String result = null;
        try {
            EntityManager em = emf.createEntityManager();
            result = (String) em.createNativeQuery("SELECT cast('foo' as varchar)").getSingleResult();
            springPlatformTransactionManager.commit(status);
        } catch (Exception e) {
            springPlatformTransactionManager.rollback(status);
            throw e;
        }
        assertEquals("foo", result);
    }

    /**
     * FIXME: Does not seem to use any transactions at all.
     */
    @Test
    @Transactional
    public void testSpring3() {
        EntityManager em = emf.createEntityManager();
        String result = (String) em.createNativeQuery("SELECT cast('foo' as varchar)").getSingleResult();
        assertEquals("foo", result);
    }
}
