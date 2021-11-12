package tr.com.cetinkaya.handterminal.daos.abstracts;

import java.util.List;

import tr.com.cetinkaya.handterminal.models.Stok;

public interface IStokDao  {

    String getLastupDate();

    void insertStok(Stok stok);

    int updateStok(Stok stok);

    Stok getStokByStokKod(String stokKod);
}
