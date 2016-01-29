import java.util.Random;
import java.util.List;
import java.util.ArrayList;

public class RandomUtil {
  private static final Random random;

  static {
    random = new Random();
  }

  /*public static class Category {
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
  }*/

  public static Category pick(List<Category> categories) {
    Category chosenCategory = null;

    int total = 0;

    for(Category category : categories) {
      if(!category.isUsed()) {
        total += category.getChance();
      }
    }

    int num = random.nextInt(total);

    for(Category category : categories) {
      if(!category.isUsed()) {
        if(category.getChance() > num) {
          chosenCategory = category;
          break;
        }

        num -= category.getChance();
      } 
    }

    return chosenCategory;
  }

  // [min, max)
  public static int range(int min, int max) {
    return random.nextInt(max - min) + min;
  }

  public static boolean chance(int in, int outOf) {
    return random.nextInt(outOf) < in;
  }

  public static int inverse(int average, double shape, int min, int max) {
    return Math.min(Math.max((int)Math.floor(inverseGaussian(average-min, shape)) + min, min), max);
  }

  public static int normal(int average, int stdDev, int min, int max) {
    return Math.min(Math.max((int)Math.round(random.nextGaussian()*stdDev + average), min), max);
  }

  //COPIED FROM SAMPLE CODE
  //https://en.wikipedia.org/wiki/Inverse_Gaussian_distribution
  private static double inverseGaussian(double mu, double lambda) {
    Random rand = new Random();
    double v = random.nextGaussian();   // sample from a normal distribution with a mean of 0 and 1 standard deviation
    double y = v*v;
    double x = mu + (mu*mu*y)/(2*lambda) - (mu/(2*lambda)) * Math.sqrt(4*mu*lambda*y + mu*mu*y*y);
    double test = random.nextDouble();  // sample from a uniform distribution between 0 and 1
    if (test <= (mu)/(mu + x))
      return x;
    else
      return (mu*mu)/x;
  }
}
