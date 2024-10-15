package si.um.feri.ris;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import si.um.feri.ris.controllers.AdministratorController;
import si.um.feri.ris.models.Administrator;
import si.um.feri.ris.models.Nesreca;
import si.um.feri.ris.models.Oskodovanec;
import si.um.feri.ris.repository.AdministratorRepository;
import si.um.feri.ris.repository.ListNesrec;
import si.um.feri.ris.repository.PregledOskodovancev;

import java.util.*;

@SpringBootTest
public class AdministratorControllerTest {

    @Autowired
    private ListNesrec nesrecaDao;
    @Autowired
    private AdministratorController adminController;
    @Autowired
    private AdministratorRepository adminDao;
    @Autowired
    private PregledOskodovancev oskodovancevDao;

    @BeforeEach
    public void init(){
        nesrecaDao.deleteAll();
        adminDao.deleteAll();
        oskodovancevDao.deleteAll();
    }

    public Administrator createAdmin(){
        Administrator admin = new Administrator();
        admin.setId(1);
        admin.setUporabniskoIme("admin");
        admin.setGeslo("admin");
        adminDao.save(admin);

        return  admin;
    }

    public Nesreca createNesreca(){
        Date date = new Date(2022, Calendar.DECEMBER, 12);
        Nesreca nesreca = new Nesreca(date, "ddd", "nekje");

        return nesreca;
    }

    public Oskodovanec createOskodavnec(){
        Oskodovanec oskodovanec = new Oskodovanec();
        oskodovanec.setIme("Jme");
        oskodovanec.setPriimek("Priimek");
        oskodovanec.setMocnejePoskodovan(true);

        return oskodovanec;
    }

    @Test
    @Transactional
    public void dodajNesrecoTest(){
        Administrator admin = createAdmin();
        Nesreca nesreca = createNesreca();
        nesreca.setAdministrator(admin);
        adminController.dodajNesreco(nesreca);
        List<Nesreca> seznamNesrec = nesrecaDao.findNesrecaById(nesreca.getId());

        Assertions.assertFalse(seznamNesrec.isEmpty());
        Assertions.assertEquals(nesreca.getId(), seznamNesrec.get(0).getId());
    }

    @Test
    public void dodajAdministratorjaTest(){
        Administrator admin = createAdmin();
        List<Administrator> seznamAdminov = adminDao.findByUporabniskoImeAdmin(admin.getUporabniskoIme());

        Assertions.assertEquals(admin.getId(), seznamAdminov.get(0).getId());
    }

    @Test
    public void dodajOskodovancaTest(){
        Oskodovanec oskodovanec = createOskodavnec();
        adminController.dodajOskodovanca(oskodovanec);
        Optional<Oskodovanec> seznamOskodovancev = oskodovancevDao.findById(oskodovanec.getId());
        System.out.println(seznamOskodovancev.get().getIme());

        Assertions.assertEquals(oskodovanec.getId(), seznamOskodovancev.get().getId());
    }

}
