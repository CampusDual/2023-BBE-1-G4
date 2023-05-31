package core;
import com.ontimize.dominiondiamondhotel.model.core.dao.BookingDao;
import com.ontimize.dominiondiamondhotel.model.core.service.BookingService;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
@SpringBootTest(classes = BookingService.class)
@ExtendWith(MockitoExtension.class)
public class BookingTest {

    @Mock
    BookingDao bookingDao;

    @Mock
    DefaultOntimizeDaoHelper daoHelper;

    @InjectMocks
    BookingService bookingService;


  /*  @Test
    public void testInsert() {
        EntityResult entityResult = new EntityResultMapImpl();
        entityResult.setCode(EntityResult.OPERATION_SUCCESSFUL);

        Mockito.when(this.daoHelper.insert(any(BookingDao.class),any(Map.class))).thenReturn(entityResult);

        Map map = new HashMap();
        map.put("entry_date","2023-05-15");
        map.put("exit_date","2023-06-05");
        map.put("hotel_id",1);
        map.put("customer_id",6);

        bookingService.bookingInsert(map);
        assertEquals(0,entityResult.getCode());

    }
*/

}