package tr.com.cetinkaya.handterminal.business.concretes;

import tr.com.cetinkaya.handterminal.business.abstracts.IBarkodBO;
import tr.com.cetinkaya.handterminal.daos.abstracts.IBarkodDao;
import tr.com.cetinkaya.handterminal.daos.abstracts.IKullaniciDao;
import tr.com.cetinkaya.handterminal.models.Barkod;

public class BarkodBO implements IBarkodBO {

    private IBarkodDao barkodDao;

    public BarkodBO(IBarkodDao barkodDao) {
        this.barkodDao = barkodDao;
    }

    @Override
    public Barkod getBarkodWithBarkod(String barkodu) {
        return barkodDao.getBarkodWithBarkod(barkodu);
    }
}
