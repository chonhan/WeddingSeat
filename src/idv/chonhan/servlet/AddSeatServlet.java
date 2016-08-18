package idv.chonhan.servlet;

import idv.chonhan.data.ImportData;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

@SuppressWarnings("serial")
public class AddSeatServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();

		try {

			int idx = 1;
			for (String name : ImportData.nameArray) {

				Filter idFilter = new FilterPredicate("id",
						FilterOperator.EQUAL, Integer.valueOf(idx));

				Query q = new Query("CustomerSeat").setFilter(idFilter);

				PreparedQuery pq = datastore.prepare(q);
				List<Entity> csList = pq.asList(FetchOptions.Builder
						.withLimit(5));

				Entity cs;

				if (csList.isEmpty()) {
					cs = new Entity("CustomerSeat");
				} else {
					cs = csList.get(0);
				}

				cs.setProperty("id", idx);
				cs.setProperty("name", name);
				cs.setProperty("seat", ImportData.seatArray[idx - 1]);
				cs.setProperty("status", 0);
				cs.setProperty("isVeg", ImportData.vegArray[idx - 1]);
				cs.setProperty("isChild", ImportData.childArray[idx - 1]);
				cs.setProperty("updateDate", new Date());
				datastore.put(cs);
				idx++;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		resp.setContentType("text/plain");
		resp.getWriter().println("Insert Success");
	}
}
