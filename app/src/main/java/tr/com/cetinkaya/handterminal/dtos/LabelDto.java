package tr.com.cetinkaya.handterminal.dtos;

import tr.com.cetinkaya.handterminal.helpers.BarkodTipi;

public class LabelDto {
    private final String stokKodu;
    private final String stokAdi;
    private final String beden;
    private final double satisFiyati;
    private final double indirimFiyati;
    private final double taksitFiyati;
    private final double birimFiyati;
    private final String birimAdi;
    private final String reyon;

    public static class Builder {
        private final String stokKodu;
        private final String stokAdi;
        private String beden = "";
        private double satisFiyati = 0.0;
        private double indirimFiyati = 0.0;
        private double taksitFiyati = 0.0;
        private double birimFiyati = 0.0;
        private String birimAdi = "";
        private final String reyon;

        public Builder(String stokKodu, String stokAdi, float satisFiyati, String reyon) {
            this.stokKodu = stokKodu;
            this.stokAdi = stokAdi;
            this.satisFiyati = satisFiyati;
            this.reyon = reyon == null ? "-" : reyon;
        }

        public Builder beden(String val) {
            beden = val;
            return this;
        }

        public Builder indirimFiyati(double val) {
            indirimFiyati = val;
            return this;
        }

        public Builder taksitFiyati(double val) {
            taksitFiyati = val;
            return this;
        }

        public Builder birimKatSayi(double val) {

            if (indirimFiyati != 0) {
                birimFiyati = indirimFiyati;
            } else {
                birimFiyati = satisFiyati;
            }
            if (val < 0) {
                birimFiyati = birimFiyati * Math.abs(val);
            } else {
                birimFiyati = birimFiyati / val;
            }
            return this;
        }

        public Builder birimAdi(String val) {
            birimAdi = val;
            return this;
        }

        public LabelDto build() {
            return new LabelDto(this);
        }
    }

    private LabelDto(Builder builder) {
        stokKodu = builder.stokKodu;
        stokAdi = builder.stokAdi;
        beden = builder.beden;
        satisFiyati = builder.satisFiyati;
        indirimFiyati = builder.indirimFiyati;
        taksitFiyati = builder.taksitFiyati;
        birimFiyati = builder.birimFiyati;
        birimAdi = builder.birimAdi;
        reyon = builder.reyon;
    }

    public String getStokKodu() {
        return stokKodu;
    }

    public String getStokAdi() {
        return stokAdi;
    }

    public String getBeden() {
        return beden;
    }

    public double getSatisFiyati() {
        return satisFiyati;
    }

    public double getIndirimFiyati() {
        return indirimFiyati;
    }

    public double getTaksitFiyati() {
        return taksitFiyati;
    }

    public double getBirimFiyati() {
        return birimFiyati;
    }

    public String getBirimAdi() {
        return birimAdi;
    }

    public String getReyon() {
        return reyon;
    }

    public boolean isReyonGrocery() {
        return reyon.compareTo("001") >= 0 && reyon.compareTo("051") <= 0;

    }

    public boolean isReyonClothes() {
        return reyon.compareTo("051") > 0;
    }
}
