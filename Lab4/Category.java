 public class Category {
    private int chance;
    private String name;
    private boolean used;

    public Category(String name, int chance) {
      this.name = name;
      this.chance = chance;
      used = false;
    }

    public int getChance() {
      return chance;
    }

    public String getName() {
      return name;
    }

    public boolean isUsed() {
      return used;
    }

    public void setUsed() {
      used = true;
    }
}