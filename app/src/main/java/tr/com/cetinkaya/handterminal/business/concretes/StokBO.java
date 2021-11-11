package tr.com.cetinkaya.handterminal.business.concretes;

import java.util.List;

import tr.com.cetinkaya.handterminal.business.abstracts.IStokBO;
import tr.com.cetinkaya.handterminal.daos.abstracts.IStokDao;
import tr.com.cetinkaya.handterminal.models.Stok;

public class StokBO implements IStokBO {
    private IStokDao stokDao;

    public StokBO(IStokDao stokDao) {
        this.stokDao = stokDao;
    }

    @Override
    public String getLastupDate() {
        return stokDao.getLastupDate();
    }

    @Override
    public void insertStok(Stok stok) {
        stokDao.insertStok(stok);
    }

    @Override
    public int updateStok(Stok stok) {
        return stokDao.updateStok(stok);
    }
}
