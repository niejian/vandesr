package cn.com.vandesr.backend.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author: nj
 * @date: 2019/2/12:下午2:38
 */
public class MyProxy<T> implements InvocationHandler {

    /**被代理的对象*/
    T target;

    public MyProxy(T target) {
        this.target = target;
    }

    /**
     * proxy: 代表动态代理对象
     * method：代表正在执行的方法
     * args：代表调用目标方法时传入的实参
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("--------------begin-------------");
        Object invoke = method.invoke(target, args);
        System.out.println("--------------end-------------");
        return invoke;

    }

    /**
     * 动态代理的具体应用
     * @param args
     * @throws Exception
     */
    public static void main(String[] args)  throws Exception{
        Subject subject = new SubjectImpl();
        InvocationHandler subjectMyProxy = new MyProxy<>(subject);
        Subject subjectProxy = (Subject) Proxy.newProxyInstance(subjectMyProxy.getClass().getClassLoader(),
                subject.getClass().getInterfaces(),
                subjectMyProxy);
        subjectProxy.hello("world");


    }
}
