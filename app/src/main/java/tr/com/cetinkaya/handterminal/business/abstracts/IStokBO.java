package tr.com.cetinkaya.handterminal.business.abstracts;

import java.util.List;

import tr.com.cetinkaya.handterminal.models.Stok;

public interface IStokBO {
    String getLastupDate();
    void insertStok(Stok stok);
    int updateStok(Stok stok);
}
