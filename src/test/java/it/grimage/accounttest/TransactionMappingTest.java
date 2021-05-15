package it.grimage.accounttest;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import it.grimage.accounttest.client.fabrick.AccountTransaction;
import it.grimage.accounttest.client.fabrick.EnumeratedValue;
import it.grimage.accounttest.configuration.ModelMapperConfiguration;
import it.grimage.accounttest.persistence.model.AccountTransactionEntity;
import it.grimage.accounttest.persistence.model.EnumeratedValueEmbedded;

@ExtendWith(SpringExtension.class)
@Import(ModelMapperConfiguration.class)
public class TransactionMappingTest {
    private static final String ENUM_TYPE = "enum1";
    private static final String ENUM_VALUE = "value1";

    @Autowired ModelMapper mapper;

    private AccountTransaction wsObject;
    private AccountTransactionEntity dbObject;

    @BeforeEach
    public void prepareObjects() {
        BigDecimal amount = new BigDecimal("120.40");
        LocalDate operationDate = LocalDate.now().minusDays(3);
        
        // setup the ws object
        wsObject = new AccountTransaction();
        wsObject.setTransactionId(UUID.randomUUID().toString());
        wsObject.setOperationId(UUID.randomUUID().toString());
        wsObject.setType(new EnumeratedValue(ENUM_TYPE, ENUM_VALUE));
        wsObject.setAmount(amount);
        wsObject.setCurrency("EUR");
        wsObject.setDescription("Pagamento");
        wsObject.setAccountingDate(operationDate);
        wsObject.setValueDate(operationDate.plusDays(2));

        // setup the database object
        dbObject = new AccountTransactionEntity();
        dbObject.setTransactionId(UUID.randomUUID().toString());
        dbObject.setOperationId(UUID.randomUUID().toString());
        dbObject.setType(new EnumeratedValueEmbedded(ENUM_TYPE, ENUM_VALUE));
        dbObject.setAmount(amount);
        dbObject.setCurrency("EUR");
        dbObject.setDescription("Pagamento");
        dbObject.setAccountingDate(operationDate);
        dbObject.setValueDate(operationDate.plusDays(2));
    }

    @Test
    public void webServiceObjectShouldMapToEntity() {
        AccountTransactionEntity entity = mapper.map(wsObject, AccountTransactionEntity.class);

        assertThat(entity).isNotNull();
        assertThat(entity.getTransactionId()).isEqualTo(wsObject.getTransactionId());
        assertThat(entity.getOperationId()).isEqualTo(wsObject.getOperationId());
        assertThat(entity.getAccountingDate()).isEqualTo(wsObject.getAccountingDate());
        assertThat(entity.getAmount()).isEqualTo(wsObject.getAmount());
        assertThat(entity.getCurrency()).isEqualTo(wsObject.getCurrency());
        assertThat(entity.getDescription()).isEqualTo(wsObject.getDescription());
        assertThat(entity.getValueDate()).isEqualTo(wsObject.getValueDate());
        assertThat(entity.getType()).extracting("enumeration", "value")
            .containsExactly(ENUM_TYPE, ENUM_VALUE);
    }

    @Test
    public void entityShouldMapToWebServiceObject() {
        AccountTransaction dto = mapper.map(dbObject, AccountTransaction.class);

        assertThat(dto).isNotNull();
        assertThat(dto.getTransactionId()).isEqualTo(dbObject.getTransactionId());
        assertThat(dto.getOperationId()).isEqualTo(dbObject.getOperationId());
        assertThat(dto.getAccountingDate()).isEqualTo(dbObject.getAccountingDate());
        assertThat(dto.getAmount()).isEqualTo(dbObject.getAmount());
        assertThat(dto.getCurrency()).isEqualTo(dbObject.getCurrency());
        assertThat(dto.getDescription()).isEqualTo(dbObject.getDescription());
        assertThat(dto.getValueDate()).isEqualTo(dbObject.getValueDate());
        assertThat(dto.getType()).extracting("enumeration", "value")
            .containsExactly(ENUM_TYPE, ENUM_VALUE);
    }
}
