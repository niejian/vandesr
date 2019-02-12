package cn.com.vandesr.backend.proxy;

/**
 * @author: nj
 * @date: 2019/2/12:下午2:57
 */
public class SubjectImpl implements Subject {

    @Override
    public void hello(String param) throws Exception {
        System.out.println("hello --->" + param);

    }
}
