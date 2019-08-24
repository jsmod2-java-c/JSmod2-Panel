package cn.jsmod2.client;

import com.alibaba.fastjson.JSON;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

public class Client {
    private static class SingletonHolder {
        static final Client instance = new Client();
    }

    public String ipAddress;

    public int port;

    public static Client getInstance(){
        return SingletonHolder.instance;
    }
    private EventLoopGroup group;
    private Bootstrap b;
    private ChannelFuture cf ;
    private ClientInitializer clientInitializer;
    private CountDownLatch lathc;

    private Client(){
        lathc = new CountDownLatch(0);
        clientInitializer = new ClientInitializer(lathc);
        group = new NioEventLoopGroup();
        b = new Bootstrap();
        b.group(group)
                .channel(NioSocketChannel.class)
                .handler(clientInitializer);
    }
    public void connect(String ip,int port){
        try {
            this.cf = b.connect(ip, port).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
        }
    }
    public ChannelFuture getChannelFuture(String ip,int port){
        if(this.cf == null) {
            this.connect(ip,port);
        }
        if(!this.cf.channel().isActive()){
            this.connect(ip,port);
        }
        return this.cf;
    }
    public void close(){
        try {
            this.cf.channel().closeFuture().sync();
            this.group.shutdownGracefully();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public String setMessage(String msg) {
        if(ipAddress == null){
            return JSON.toJSONString(new HashMap<String,String>(){
                {
                    put("status","500");
                    put("message","null");
                    put("value","null");
                }
            });
        }
        try {
            ChannelFuture cf = this.getChannelFuture(ipAddress,port);//单例模式获取ChannelFuture对象
            cf.channel().writeAndFlush(msg);
            //发送数据控制门闩加一
            lathc = new CountDownLatch(1);
            clientInitializer.resetLathc(lathc);
            lathc.await();//开始等待结果返回后执行下面的代码
            return clientInitializer.getServerResult();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        return null;
    }



}
