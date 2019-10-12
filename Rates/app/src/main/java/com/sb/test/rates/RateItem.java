package com.sb.test.rates;

public class RateItem {
    private String name;
    private String shortName;
    private Double coefficient;

    public RateItem(String name, String shortName, Double coefficient) {
        this.name = name;
        this.shortName = shortName;
        this.coefficient = coefficient;
    }

    public RateItem(RateItem copy) {
        this.name = copy.name;
        this.shortName = copy.shortName;
        this.coefficient = copy.coefficient;
    }


    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }

    public Double getCoefficient() {
        return coefficient;
    }

    public Double translate(double baseValue) {
        return baseValue * coefficient;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + name.hashCode();
        result = prime * result + shortName.hashCode();
        result = prime * result + coefficient.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RateItem other = (RateItem) obj;
        if (!name.equals(other.name))
            return false;
        if (!shortName.equals(other.shortName))
            return false;
        if (!coefficient.equals(other.coefficient))
            return false;
        return true;
    }
}
