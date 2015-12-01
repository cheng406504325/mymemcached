import java.io.Serializable;
import java.util.List;

/**
 * @author suncheng
 * @version 0.0.1
 * @since 15/12/1
 */
public class Module implements Serializable {
    private String name;
    private Integer age;
    private List<String> stus;

    @Override
    public String toString() {
        return "Module{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", stus=" + stus +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public List<String> getStus() {
        return stus;
    }

    public void setStus(List<String> stus) {
        this.stus = stus;
    }
}
