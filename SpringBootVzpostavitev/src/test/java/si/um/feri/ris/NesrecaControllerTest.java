package si.um.feri.ris;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import si.um.feri.ris.models.Nesreca;
import si.um.feri.ris.repository.ListNesrec;

@SpringBootTest
public class NesrecaControllerTest {

    @Autowired
    private ListNesrec nesrecaDao;


}
