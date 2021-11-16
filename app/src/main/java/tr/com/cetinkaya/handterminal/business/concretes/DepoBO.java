package tr.com.cetinkaya.handterminal.business.concretes;

import tr.com.cetinkaya.handterminal.business.abstracts.IDepoBO;
import tr.com.cetinkaya.handterminal.daos.abstracts.IDepoDao;
import tr.com.cetinkaya.handterminal.daos.abstracts.IKullaniciDao;
import tr.com.cetinkaya.handterminal.models.Depo;

public class DepoBO implements IDepoBO {

    private IDepoDao depoDao;

    public DepoBO(IDepoDao depoDao) {
        this.depoDao = depoDao;
    }


    @Override
    public Depo getDepoById(int id) {
        return depoDao.getDepoById(id);
    }

    @Override
    public int updateDepo(Depo depo) {
        return depoDao.updateDepo(depo);
    }

    @Override
    public void insertDepo(Depo depo) {
        depoDao.insertDepo(depo);
    }
}
