package com.test.seckill;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

/**
 * Servlet implementation class DoSecKill
 */
public class DoSecKill extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DoSecKill() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String userid = new Random().nextInt(10000) + "";
		String proid = request.getParameter("proid");
		boolean flag = SecKill_redisByScript.doSecKill(userid, proid);

		response.getWriter().print(flag);
	}
 
	public boolean doSecKille(String userid, String proid) {
		String qtkey = "sk:" + proid + ":qt";
		String userkey = "sk:01:user";
//		Jedis jedis = new Jedis("192.168.17.128", 6379);
		//使用jedis连接池
		JedisPool jedisPool = JedisPoolUtil.getJedisPoolInstance();
		Jedis jedis = jedisPool.getResource();
		//判断此人是否抢到过
		//开启乐观锁
		Boolean flag = jedis.sismember(userkey, userid);
		jedis.watch(qtkey);
		if(!flag){
			if (Integer.valueOf(jedis.get(qtkey)) > 0) {
				Transaction transaction = jedis.multi();
				// 减库存
				transaction.decr(qtkey);
				// 购买的用户
				transaction.sadd(userkey, userid);
				List<Object> exec = transaction.exec();
				if(exec.size()==0||exec==null){
					System.err.println("抢购失败！");
				}else{
					System.out.println("抢购成功！"+userid);
				}
				System.out.println("活跃的连接数："+jedisPool.getNumActive()+"-----------等待的连接数"+jedisPool.getNumWaiters());
				jedis.close();
				return true;
			}
		}
		
		jedis.close();
		return false;
	}

}
