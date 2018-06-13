package com.ibanity.apis.client.sandbox.services;

import com.ibanity.apis.client.exceptions.ResourceNotFoundException;
import com.ibanity.apis.client.models.FinancialInstitution;
import com.ibanity.apis.client.sandbox.services.impl.SandboxFinancialInstitutionsServiceImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.Assert.assertTrue;

/**
 * SandboxFinancialInstitutionsServiceImpl Tester.
 *
 * @author Daniel De Luca
 * @version 1.0
 * @since <pre>Jun 13, 2018</pre>
 */
public class SandboxFinancialInstitutionsServiceTest {

    private static final SandboxFinancialInstitutionsService sandboxFinancialInstitutionsService = new SandboxFinancialInstitutionsServiceImpl();
    private static Instant now;

    private static final String TEST_CASE = SandboxFinancialInstitutionsServiceTest.class.getSimpleName();
    private static String name;

    @Before
    public void before()  {
    }

    @After
    public void after()  {
    }

    /**
     * Method: createFinancialInstitution(FinancialInstitution financialInstitution)
     */
    @Test
    public void testCreateFinancialInstitution() throws Exception {
        FinancialInstitution newFinancialInstitution = createFinancialInstitution();
        assertTrue(newFinancialInstitution.getId() != null);
        assertTrue(name.equals(newFinancialInstitution.getName()));
        assertTrue(newFinancialInstitution.getSandbox());
    }

    public static FinancialInstitution createFinancialInstitution() {
        now = Instant.now();
        name = TEST_CASE + "-" + now.toString();
        FinancialInstitution newFinancialInstitution = new FinancialInstitution();
        newFinancialInstitution.setSandbox(Boolean.TRUE);
        newFinancialInstitution.setName(name);
        return sandboxFinancialInstitutionsService.createFinancialInstitution(newFinancialInstitution);
    }

    public static void deleteFinancialInstitution(UUID financialInstitutionId) throws ResourceNotFoundException {
        sandboxFinancialInstitutionsService.deleteFinancialInstitution(financialInstitutionId);
    }

    /**
     * Method: updateFinancialInstitution(FinancialInstitution financialInstitution)
     */
    @Test
    public void testUpdateFinancialInstitution() throws Exception {
        FinancialInstitution newFinancialInstitution = createFinancialInstitution();
        newFinancialInstitution.setName(newFinancialInstitution.getName()+"-UPDATED");
        FinancialInstitution updatedFinancialInstitution = sandboxFinancialInstitutionsService.updateFinancialInstitution(newFinancialInstitution);
        assertTrue((newFinancialInstitution.getName()).equals(updatedFinancialInstitution.getName()));
        assertTrue(newFinancialInstitution.getId().equals(updatedFinancialInstitution.getId()));
        assertTrue(newFinancialInstitution.getSandbox().equals(updatedFinancialInstitution.getSandbox()));
    }

    /**
     * Method: deleteFinancialInstitution(UUID financialInstitutionId)
     */
    @Test (expected = ResourceNotFoundException.class)
    public void testDeleteFinancialInstitution() throws Exception {
        FinancialInstitution newFinancialInstitution = createFinancialInstitution();
        sandboxFinancialInstitutionsService.deleteFinancialInstitution(newFinancialInstitution.getId());
        sandboxFinancialInstitutionsService.getFinancialInstitution(newFinancialInstitution.getId());
    }

    /**
     * Method: getFinancialInstitution(UUID financialInstitutionId)
     */
    @Test
    public void testGetFinancialInstitution() throws Exception {
        FinancialInstitution newFinancialInstitution = createFinancialInstitution();
        FinancialInstitution getFinancialInstitution = sandboxFinancialInstitutionsService.getFinancialInstitution(newFinancialInstitution.getId());
        assertTrue(newFinancialInstitution.equals(getFinancialInstitution));
    }

    /**
     * Method: getFinancialInstitution(UUID financialInstitutionId)
     */
    @Test (expected = ResourceNotFoundException.class)
    public void testGetFinancialInstitutionUnknownID() throws Exception {
        FinancialInstitution getFinancialInstitution = sandboxFinancialInstitutionsService.getFinancialInstitution(UUID.randomUUID());
    }
}
