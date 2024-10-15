package si.um.feri.ris;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import si.um.feri.ris.controllers.UporabnikController;
import si.um.feri.ris.models.Donacija;
import si.um.feri.ris.models.Uporabnik;
import si.um.feri.ris.repository.PregledDonacij;
import si.um.feri.ris.repository.UporabnikRepository;

import java.util.List;

import jakarta.transaction.Transactional;

@SpringBootTest
public class UporabnikControllerTest {

    @Autowired
    private UporabnikRepository uporabnikDao;

    @Autowired
    private PregledDonacij donacijaDao;

    @Autowired
    private UporabnikController uporabnikController;

    @BeforeEach
    public void init() {
        uporabnikDao.deleteAll();
        donacijaDao.deleteAll();
    }

    @Test
    @Transactional //zaradi lazyLoading - da se vse commita in potem preveri
    public void getVeckratneDonatorje() {
        Uporabnik uporabnik1 = new Uporabnik("uporabnisko_ime", "ime", "priimek");
        uporabnikDao.save(uporabnik1);

        Uporabnik uporabnik2 = new Uporabnik("uporabnisko_ime2", "ime2", "priimek2");
        uporabnikDao.save(uporabnik2);

        Donacija donacija1 = new Donacija();
        donacija1.setZnesekDonacije(200);
        uporabnikController.dodajDonacijoUporabniku(uporabnik1.getId(), donacija1);

        Donacija donacija2 = new Donacija();
        donacija2.setZnesekDonacije(2000);
        uporabnikController.dodajDonacijoUporabniku(uporabnik2.getId(), donacija2);

        Donacija donacija3 = new Donacija();
        donacija3.setZnesekDonacije(200);
        uporabnikController.dodajDonacijoUporabniku(uporabnik1.getId(), donacija3);

        List<Uporabnik> veckratniDonatorji = uporabnikController.pridobiVeckratneDonatorje();

        Assertions.assertEquals(uporabnik1.getIme(), veckratniDonatorji.get(0).getUporabniskoIme()); //v bazi je uporabnisko ime kot ime
    }

    //Donirali vec kot 3x in je SUM vec od 500
    @Test
    @Transactional
    public void pridobiTopDonatorje(){
        Uporabnik uporabnik1 = new Uporabnik("uporabnisko_ime", "ime", "priimek");
        uporabnikDao.save(uporabnik1);

        Donacija d1 = new Donacija();
        Donacija d2 = new Donacija();
        Donacija d3 = new Donacija();

        d1.setZnesekDonacije(100);
        d2.setZnesekDonacije(500);
        d3.setZnesekDonacije(100);

        uporabnikController.dodajDonacijoUporabniku(uporabnik1.getId(), d1);
        uporabnikController.dodajDonacijoUporabniku(uporabnik1.getId(), d2);
        uporabnikController.dodajDonacijoUporabniku(uporabnik1.getId(), d3);

        List<Uporabnik> topDonatorji = uporabnikController.pridobiTopDonatorje();

        Assertions.assertEquals(uporabnik1.getUporabniskoIme(), topDonatorji.get(0).getUporabniskoIme());
    }
}