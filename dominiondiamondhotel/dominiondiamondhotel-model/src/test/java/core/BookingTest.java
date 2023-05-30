package core;
import com.ontimize.dominiondiamondhotel.model.core.dao.BookingDao;
import com.ontimize.dominiondiamondhotel.model.core.service.BookingService;
import com.ontimize.jee.common.dto.EntityResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;


public class BookingTest {

    @Mock
    BookingDao bookingDao;

    @InjectMocks
    BookingService bookingService;

    @Mock
    EntityResult entityResult;

    @Test
    public void testInsert() {

        

    }
}