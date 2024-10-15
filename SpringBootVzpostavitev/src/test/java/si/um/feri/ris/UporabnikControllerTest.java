package si.um.feri.ris;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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


}
