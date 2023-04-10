package com.mangokiddo.gateway.bind;

import net.sf.cglib.core.Signature;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InterfaceMaker;
import org.apache.dubbo.rpc.service.GenericService;
import org.objectweb.asm.Type;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: 代理工厂
 * @author: mango
 * @time: 2023/4/7 16:20
 */
public class GenericReferenceProxyFactory {

    /**
     * PRC泛化调用服务
     */
    private final GenericService genericService;

    /**
     * 代理对象缓存
     */
    private final Map<String,IGenericReference> genericReferenceCache = new ConcurrentHashMap<>();

    public GenericReferenceProxyFactory(GenericService genericService) {
        this.genericService = genericService;
    }


    /**
     * 返回一个代理类（从缓存中获取，如果不存在就创建一个放入缓存中并返回）
     * @param methodName
     * @return
     */
    public IGenericReference newInstance(String methodName){

        return genericReferenceCache.computeIfAbsent(methodName,e -> {

            GenericReferenceProxy genericReferenceProxy = new GenericReferenceProxy(genericService, methodName);
            //创建生成代理类模版

            InterfaceMaker interfaceMaker = new InterfaceMaker();

            interfaceMaker.add(new Signature(methodName, Type.getType(String.class),new Type[]{Type.getType(String.class)}),null);

            Class<?> interfaceClass = interfaceMaker.create();

            //代理对象
            Enhancer enhancer = new Enhancer();

            //代理类实现通用接口
            enhancer.setInterfaces(new Class[]{interfaceClass,IGenericReference.class});

            /**
             *  enhance.callback():
             * 它用于设置代理对象的回调函数，也就是当代理对象的方法被调用时将执行的代码块。
             * 回调函数可以是一个接口的实现类，也可以是一个MethodInterceptor对象，该对象可以拦截所有被代理对象的方法调用并进行自定义处理。
             * 这使得我们可以在代理对象的方法执行前后做一些额外的操作，比如记录日志、性能监控等。
             */
            enhancer.setCallback(genericReferenceProxy);
            return (IGenericReference) enhancer.create();
        });
    }
}
