package rpc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;

import db.DBConnection;
import db.DBConnectionFactory;
import entity.Item;

import java.util.logging.Level;
import java.util.logging.Logger;

//sudo rm /opt/tomcat/webapps/Titan.war
//http://52.40.235.88/Titan/


/**
 * Servlet implementation class Searchitem
 */
@WebServlet("/search")
public class Searchitem extends HttpServlet {
	private DBConnection conn = DBConnectionFactory.getDBConnection();
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(Searchitem.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Searchitem() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException 
	
			 {
				// HttpSession session = request.getSession();
				// if (session.getAttribute("user") == null) {
				// 	response.setStatus(403);
				// 	return;
				// }

				String userId = "1111";
				double lat = Double.parseDouble(request.getParameter("lat"));
				double lon = Double.parseDouble(request.getParameter("lon"));
				// Term can be empty or null.
				String term = request.getParameter("term");
				LOGGER.log(Level.INFO, "lat:" + lat + ",lon:" + lon);
				List<Item> items = conn.searchItems(userId, lat, lon, term);
				List<JSONObject> list = new ArrayList<>();

				Set<String> favorite = conn.getFavoriteItemIds(userId);
				try {
					for (Item item : items) {
						JSONObject obj = item.toJSONObject();
						obj.put("favorite", favorite.contains(item.getItemId()));
						list.add(obj);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				JSONArray array = new JSONArray(list);
				RpcHelper.writeJsonArray(response, array);
			}

			
			//old version
//	{
//
//		HttpSession session = request.getSession();
//		if (session.getAttribute("user") == null) {
//			response.setStatus(403);
//			return;
//		}
//
//		String userId = session.getAttribute("user").toString();
//		
//		
//		
//	//	String userId = request.getParameter("user_id");
//		double lat = Double.parseDouble(request.getParameter("lat"));
//		double lon = Double.parseDouble(request.getParameter("lon"));
//		// Term can be empty or null.
//		String term = request.getParameter("term");
//		List<Item> items = conn.searchItems(userId, lat, lon, term);
//		List<JSONObject> list = new ArrayList<>();
//		Set<String> favorite = conn.getFavoriteItemIds(userId);// 就是这行
//
//		try {
//			for (Item item : items) {
//				JSONObject obj = item.toJSONObject();
//				list.add(obj);
//				obj.put("favorite", favorite.contains(item.getItemId()));// 还有这行
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		JSONArray array = new JSONArray(list);
//		RpcHelper.writeJsonArray(response, array);
//
//
//	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
