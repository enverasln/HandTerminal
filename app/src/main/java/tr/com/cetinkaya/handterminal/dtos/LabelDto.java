package tr.com.cetinkaya.handterminal.dtos;

public class LabelDto {
    private final String barkod;
    private final String stokKodu;
    private final String stokAdi;
    private final String beden;
    private final double etiketFiyati;
    private final double satisFiyati;
    private final double taksitFiyati;
    private final double birimKatsayi;
    private final String birimAdi;
    private final String reyon;
    private String fiyatDegTarihi = "";
    private int yerliUretim = 0;
    private String mensei;


    public static class Builder {
        private final String barkod;
        private final String stokKodu;
        private final String stokAdi;
        private String beden = "";
        private double etiketFiyati = 0.0;
        private double satisFiyati;
        private double taksitFiyati = 0.0;
        private double birimKatsayi = 0.0;
        private String birimAdi = "";
        private final String reyon;
        private String fiyatDegTarihi = "";
        private int yerliUretim = 0;
        private String mensei;

        public Builder(String barkod, String stokKodu, String stokAdi, double satisFiyati, String reyon) {
            this.barkod = barkod;
            this.stokKodu = stokKodu;
            this.stokAdi = stokAdi;
            this.satisFiyati = satisFiyati;
            this.reyon = reyon == null ? "-" : reyon;
        }

        public Builder beden(String val) {
            beden = val;
            return this;
        }

        public Builder etiketFiyati(double val) {
            etiketFiyati = val;
            return this;
        }

        public Builder taksitFiyati(double val) {
            taksitFiyati = val;
            return this;
        }

        public Builder birimKatSayi(double val) {
            birimKatsayi = val;
            return this;
        }

        public Builder birimAdi(String val) {
            birimAdi = val;
            return this;
        }

        public Builder fiyatDegTarihi(String val) {
            this.fiyatDegTarihi = val;
            return this;
        }

        public Builder yerliUretim(int val) {
            this.yerliUretim = val;
            return this;
        }

        public Builder mensei(String val) {
            this.mensei = val;
            return this;
        }

        public LabelDto build() {
            return new LabelDto(this);
        }
    }

    private LabelDto(Builder builder) {
        barkod = builder.barkod;
        stokKodu = builder.stokKodu;
        stokAdi = builder.stokAdi;
        beden = builder.beden;
        etiketFiyati = builder.etiketFiyati;
        satisFiyati = builder.satisFiyati;
        taksitFiyati = builder.taksitFiyati;
        birimKatsayi = builder.birimKatsayi;
        birimAdi = builder.birimAdi;
        reyon = builder.reyon;
        fiyatDegTarihi = builder.fiyatDegTarihi;
        yerliUretim = builder.yerliUretim;
        mensei = builder.mensei;
    }

    public String getBarkod() {
        return barkod;
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

    public double getEtiketFiyati() {
        return etiketFiyati;
    }

    public double getSatisFiyati() {
        return satisFiyati;
    }

    public double getTaksitFiyati() {
        return taksitFiyati;
    }

    public double getBirimFiyati() {
        return calculateBirimFiyat(satisFiyati);
    }

    private double calculateBirimFiyat(double fiyat) {
        double birimFiyati;
        if (birimKatsayi < 0) {
            birimFiyati = fiyat * Math.abs(birimKatsayi);
        } else {
            birimFiyati = fiyat / birimKatsayi;
        }
        return birimFiyati;
    }



    public String getBirimAdi() {
        return birimAdi.isEmpty() ? "-" : birimAdi;
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

    public double getIndirimOrani() {
        double indirimTutari = etiketFiyati - satisFiyati;
        double indirimOrani = (indirimTutari * 100) / satisFiyati;
        return indirimOrani;
    }

    public String getFiyatDegTarihi() {
        return fiyatDegTarihi;
    }

    public int getYerliUretim() {
        return yerliUretim;
    }

    public String getMensei() {
        return mensei.isEmpty() ? "-" : mensei;
    }
}
