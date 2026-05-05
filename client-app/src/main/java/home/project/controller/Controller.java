package home.project.controller;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import com.google.gson.Gson;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import home.project.beans.AnnouncementBean;
import home.project.beans.PromotionBean;
import home.project.beans.RentalBean;
import home.project.beans.UserBean;
import home.project.container.PasswordContainer;
import home.project.container.VehicleContainer;
import home.project.dto.User;
import home.project.logger.ClientLogger;
import home.project.model.Announcement;
import home.project.model.Bicycle;
import home.project.model.Car;
import home.project.model.Rental;
import home.project.model.Scooter;
import home.project.service.RentalService;
import home.project.service.UserService;
import home.project.service.VehicleService;
import home.project.utility.ServerUtility;

@MultipartConfig
@WebServlet("/Controller")
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static final String CAR = "car";
	private static final String SCOOTER = "scooter";
	private static final String BICYCLE = "bicycle";
       
	public Controller() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response){
		
		try {
			boolean forwardFlag = true;
			
			request.setCharacterEncoding("UTF-8");
			HttpSession session = request.getSession();

			String paggesAddress = "WEB-INF/pages/";
			String address = paggesAddress + "login.jsp";

			String action = request.getParameter("action");
			session.setAttribute("notification", "");
			UserBean userBean = (UserBean) session.getAttribute("userBean");
			
			if(request.getQueryString() == null || "".equals(action))
				address = paggesAddress + "login.jsp";
			else if("login".equals(action)) {
				
				if(userBean != null && userBean.isLoggedIn())
					address = paggesAddress + "home.jsp";
				else {
					String userName = request.getParameter("userName");
					String password = request.getParameter("password");
					
					userBean = UserService.login(userName, password);
					if(userBean != null) {
						session.setAttribute("userBean", userBean);
						address = paggesAddress + "home.jsp";
						
					} else {
						session.setAttribute("notification", "Wrong credentials!");
						address = paggesAddress + "login.jsp";
					}
				}	
				
			} else if("logout".equals(action)) {
				
				session.invalidate();
				address = paggesAddress + "login.jsp";
				
			} else if (userBean != null && userBean.isLoggedIn() &&  request.getQueryString().startsWith("action=fetch")) {
			    
				response.setHeader("Access-Control-Allow-Origin", "*"); // Dozvoli pristup sa bilo kojeg domena
				response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, DELETE"); // Dozvoljene metode
				response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization"); // Dozvoljena zaglavlja
				
			    String[] tmp = request.getQueryString().split("&");
				String type = tmp[1].split("=")[1];

			    if (type != null && !type.isEmpty()) {
			    	
			    	VehicleContainer<Scooter> scooters = null;
			    	VehicleContainer<Car> cars = null;
			    	VehicleContainer<Bicycle> bicycles = null;
			    	
			    	Gson gson = new Gson();
			    	String json = "";
			    	
			    	if(CAR.equals(type)) {
			    		cars = VehicleService.getCars();
			    		json = gson.toJson(cars);
			    	} else if(SCOOTER.equals(type)) {
			    		scooters = VehicleService.getScooters();
			    		json = gson.toJson(scooters);
			    	} else if(BICYCLE.equals(type)) {
			    		bicycles = VehicleService.getBicycles();
			    		json = gson.toJson(bicycles);
			    	}

			    	response.setContentType("application/json");
			        response.setCharacterEncoding("UTF-8");
			        response.getWriter().write(json); // Slanje JSON odgovora klijentu
			    
			    } else {
			        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			        response.getWriter().write("{\"error\": \"Invalid vehicle type\"}");
			    }
			    forwardFlag = false;
			
			} else if(userBean != null && userBean.isLoggedIn() &&  "profile".equals(action)) {
		        
				ArrayList<Rental> rentals = RentalService.getRentals(userBean.getUser().getUserName());
				RentalBean rentalBean = new RentalBean();
				
				rentalBean.setRentals(rentals);
				request.setAttribute("rentalBean", rentalBean);
				
				ArrayList<PromotionBean> pBeans = new ArrayList<>();
				ArrayList<AnnouncementBean> aBeans = new ArrayList<>();
				
				List<Announcement> announcemens = ServerUtility.getAnnouncementsFromRss();
				if(announcemens != null) {
					for(Announcement ann : announcemens) {
						if(ann.IsPromotion()) {
							PromotionBean pb = new PromotionBean();
							pb.setTitle(ann.getTitle());
							pb.setDescription(ann.getContent());
							pb.setExpirationDate(ann.getExpirationDate().toString());
							pBeans.add(pb);
						} else {
							AnnouncementBean ab = new AnnouncementBean();
							ab.setTitle(ann.getTitle());
							ab.setContent(ann.getContent());
							aBeans.add(ab);
						}
					}
				}
				
				session.setAttribute("pBeans", pBeans);
				session.setAttribute("aBeans", aBeans);
				
				address = paggesAddress + "profile.jsp";
				forwardFlag = true;
			
			} else if(userBean != null && userBean.isLoggedIn() &&  "back".equals(action)) {
				address = paggesAddress + "home.jsp";
				forwardFlag = true;
			} else if("create".equals(action)) {
				address = paggesAddress + "create.jsp";
				forwardFlag = true;
			} else if(userBean != null && userBean.isLoggedIn() && "promotions".equals(action)) {
				
				ArrayList<PromotionBean> pBeans = new ArrayList<>();
				
				List<Announcement> announcemens = ServerUtility.getAnnouncementsFromRss();
				if(announcemens != null) {
					for(Announcement ann : announcemens) {
						if(ann.IsPromotion()) {
							PromotionBean pb = new PromotionBean();
							pb.setTitle(ann.getTitle());
							pb.setDescription(ann.getContent());
							pb.setExpirationDate(ann.getExpirationDate().toString());
							pBeans.add(pb);
						}
					}
				}
				String jsonResponse = new Gson().toJson(pBeans);
				
				response.setContentType("application/json"); // Postavljanje Content-Type headera
			    response.setCharacterEncoding("UTF-8");     // Omogućavanje UTF-8 kodiranja za specijalne karaktere
			    response.getWriter().write(jsonResponse);
				
				forwardFlag = false;
				
			} else if(userBean != null && userBean.isLoggedIn() &&  "announcements".equals(action)) {
				
				ArrayList<AnnouncementBean> aBeans = new ArrayList<>();
				
				List<Announcement> announcemens = ServerUtility.getAnnouncementsFromRss();
				if(announcemens != null) {
					for(Announcement ann : announcemens) {
						if(!ann.IsPromotion()) {
							AnnouncementBean ab = new AnnouncementBean();
							ab.setTitle(ann.getTitle());
							ab.setContent(ann.getContent());
							aBeans.add(ab);
						}
					}
				}
				
				String jsonResponse = new Gson().toJson(aBeans);
				
				response.setContentType("application/json"); // Postavljanje Content-Type headera
			    response.setCharacterEncoding("UTF-8");     // Omogućavanje UTF-8 kodiranja za specijalne karaktere
			    response.getWriter().write(jsonResponse);
				forwardFlag = false;
			}
			
			if(forwardFlag) {
				RequestDispatcher dispatcher = request.getRequestDispatcher(address);
				dispatcher.forward(request, response);
			}
			
		} catch (UnsupportedEncodingException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			e.printStackTrace();
			ClientLogger.logWarning("Returning BAD REQUEST due to invalid or unprocessable request.");
			
			try {
				response.getWriter().write("{\"error\": \"The request was invalid or cannot be processed.\"}");
		    } catch (IOException ioException) {
		        ClientLogger.logError("IOException during response handling: " + ioException.getMessage());
		    }
			
		} catch (IOException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			e.printStackTrace();
			ClientLogger.logWarning("Returning BAD REQUEST due to invalid or unprocessable request.");
			
			try {
				response.getWriter().write("{\"error\": \"The request was invalid or cannot be processed.\"}");
		    } catch (IOException ioException) {
		        ClientLogger.logError("IOException during response handling: " + ioException.getMessage());
		    }
			
		} catch (ServletException e) {
		    ClientLogger.logError("ServletException occurred: " + e.getMessage());
		    e.printStackTrace();
		    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		    response.setContentType("application/json");
		    response.setCharacterEncoding("UTF-8");

		    try {
		        response.getWriter().write("{\"error\": \"An internal server error occurred.\"}");
		    } catch (IOException ioException) {
		        ClientLogger.logError("IOException during response handling: " + ioException.getMessage());
		    }
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response){
		
		try {
			boolean forwardFlag = false;
			
			String action = request.getParameter("action");
			String address = "";
			
			if("login".equals(action) || "profile".equals(action) || "logout".equals(action) || "back".equals(action) || "create".equals(action))
				doGet(request, response);
			else if(action == null) {
					System.out.println("ACTION IS NULL!");
			} else if("newProfile".equals(action)) {
				
				// Dohvatanje drugih podataka iz forme
		        String firstName = request.getParameter("firstName");
		        String password = request.getParameter("password");
		        String lastName = request.getParameter("lastName");
		        String userName = request.getParameter("userName");
		        String idCard = request.getParameter("idCard");
		        String email = request.getParameter("email");
		        String phoneNumber = request.getParameter("phoneNumber");

		        // Dohvatanje fajla za avatar
				Part filePart = request.getPart("avatar");
				byte[] avatarBytes;
				String picture = null;
				
				if (filePart != null) {
					try (InputStream inputStream = filePart.getInputStream()) {
						avatarBytes = convertToByteArray(inputStream);
						picture = Base64.getEncoder().encodeToString(avatarBytes);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

		        User user = new User(userName, password, firstName, lastName, idCard, email, phoneNumber, picture, false);
		        String notification = UserService.createUser(user) ? "Profile created successfully!" : "Error: Profile could not be created. Please try again."; 
		        request.setAttribute("create_notification", notification);
		        
		        address = "WEB-INF/pages/create.jsp";
		        forwardFlag = true;
				
			} else {
				HttpSession session = request.getSession();
		        Object userBeanObj = session.getAttribute("userBean");
		        
		        if (userBeanObj == null || !(userBeanObj instanceof UserBean)) {
		            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Korisnik nije ulogovan.");
		            return;
		        }
		        
		        UserBean userBean = (UserBean) userBeanObj;
				request.setCharacterEncoding("UTF-8");
				
				if("rent".equals(action)) {
					forwardFlag = false;
					
					response.setHeader("Access-Control-Allow-Origin", "*"); // Dozvoli pristup sa bilo kojeg domena
					response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, DELETE"); // Dozvoljene metode
					response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization"); // Dozvoljena zaglavlja
					
				    StringBuilder sb = new StringBuilder();
				    String line;
				    
				    try (BufferedReader reader = request.getReader()) {
				        while ((line = reader.readLine()) != null) {
				            sb.append(line);
				        }
				    }

				    String body = sb.toString();
				    String vehicleId = extractVehicleIdFromJson(body);

				    // Provjera da li je ID uspješno pročitan
				    if (vehicleId != null && !vehicleId.isEmpty()) {
				        // Logika za obradu zahtjeva
				        boolean isRented = VehicleService.setRentedVehicleStatus(vehicleId);

				        if (isRented) {
				            response.setStatus(HttpServletResponse.SC_OK); // 200 OK
				            response.getWriter().write("OK");
				        } else {
				            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 Bad Request
				            response.getWriter().write("NOK");
				        }
				    } else {
				        response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 Bad Request
				        response.getWriter().write("Invalid ID");
				    }
				} else if("save".equals(action)) {
					forwardFlag = false;
					
					BufferedReader reader = request.getReader();
			        StringBuilder jsonBuilder = new StringBuilder();
			        
			        String line;
			        while ((line = reader.readLine()) != null)
			            jsonBuilder.append(line);

			        String jsonData = jsonBuilder.toString();
			        Rental rental = new Gson().fromJson(jsonData, Rental.class);
			        
			        rental.setUserName(userBean.getUser().getUserName());
			        rental.setDateTime(new Date().toString());
			        
			        VehicleService.setFreeVehicleStatus(rental.getVehicleID());
			        if(RentalService.insertRental(rental)) {
			        	
			        	// 3. Kreiraj PDF dokument u memoriji
				        ByteArrayOutputStream baos = new ByteArrayOutputStream();

				        try {
				        	Document document = new Document();
				        	PdfWriter.getInstance(document, baos);
				        	document.open();

				        	// Naslov
				        	Paragraph header = new Paragraph("Vehicle rental certificate", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16));		        	header.setAlignment(Element.ALIGN_CENTER);
				        	header.setAlignment(Element.ALIGN_CENTER);
				        	document.add(header);

				        	// Horizontalna linija
				        	document.add(new Paragraph(" "));
				        	document.add(new LineSeparator());
				        	document.add(new Paragraph(" "));

				        	// Klijent
				        	document.add(new Paragraph("=========================================="));
				        	document.add(new Paragraph("Client: " + rental.getUserName(), FontFactory.getFont(FontFactory.HELVETICA, 12)));
				        	document.add(new Paragraph("=========================================="));

				        	// Lokacije i vreme
				        	document.add(new Paragraph("Date & Time: " + rental.getDateTime()));
				        	document.add(new Paragraph("Start location: " + rental.getStartX() + ", " + rental.getStartY()));
				        	document.add(new Paragraph("End location: " + rental.getEndX() + ", " + rental.getEndY()));
				        	
				        	String vehicleType = "";
				        	if(rental.getVehicleID().substring(0, 1).equals("S"))
				        		vehicleType = SCOOTER;
				        	else if(rental.getVehicleID().substring(0, 1).equals("B"))
				        		vehicleType = BICYCLE;
				        	else if(rental.getVehicleID().substring(0, 1).equals("C"))
				        		vehicleType = CAR;
				        	
				        	document.add(new Paragraph("Vehicle type: " + vehicleType));
				        	if(CAR.equals(vehicleType)) {
				        		document.add(new Paragraph("Licence number: " + rental.getLicenceNumber()));
				        		document.add(new Paragraph("Document number: " + rental.getDocumentNumber()));
				        	}
				        	
				        	document.add(new Paragraph("Vehicle ID: " + rental.getVehicleID()));
				        	document.add(new Paragraph("=========================================="));
				        	document.add(new Paragraph("Total: " + rental.getPrice() + " $"));
				        	document.add(new Paragraph("=========================================="));
				        	
				        	// Opcioni razmak
				        	document.add(new Paragraph(" "));
				        	document.add(new LineSeparator());
				        	document.add(new Paragraph("Thank you for using our service! Contact us on: info@etfbl_ip.com", FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 11)));
				        	document.close();
				            
				        } catch (DocumentException e) {
				            e.printStackTrace();
				            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Greška pri kreiranju PDF-a.");
				            return;
				        }

				        // 4. Postavi zaglavlja za preuzimanje PDF-a
				        response.setContentType("application/pdf");
				        String filename = "invoice_" + rental.getUserName() + "_" + System.currentTimeMillis() + ".pdf";
				        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
				        response.setContentLength(baos.size());

				        // 5. Pošalji PDF kao odgovor
				        ServletOutputStream out = response.getOutputStream();
				        baos.writeTo(out);
				        out.flush();
			        	
			        } else {
			        	response.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED, "Greška pri cuvanju iznajmljivanja.");
			        }
				} else if("pass".equals(action)) {
			    	forwardFlag = false;
			    	
			    	BufferedReader reader = request.getReader();
			        StringBuilder jsonBuilder = new StringBuilder();
			        
			        String line;
			        while ((line = reader.readLine()) != null)
			            jsonBuilder.append(line);

			        String jsonData = jsonBuilder.toString();
			        PasswordContainer container = new Gson().fromJson(jsonData, PasswordContainer.class);
					
			        boolean result = UserService.changePassword(userBean.getUser().getUserName(), container);
			        if(result)
			        	response.setStatus(HttpServletResponse.SC_OK);
			        else
			        	response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				
				} else if("deactivate".equals(action)) {
					if(UserService.deactivateAccount(userBean.getUser().getUserName())) {
						session.invalidate();
						address = "/WEB-INF/pages/login.jsp";
						forwardFlag = true;
					}
				}
			}
			
			if(forwardFlag) {
				RequestDispatcher dispatcher = request.getRequestDispatcher(address);
				dispatcher.forward(request, response);
			}
			
		} catch(ServletException e) {
			e.printStackTrace();
			ClientLogger.logError("ServletException occurred: " + e.getMessage());

		    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		    response.setContentType("application/json");
		    response.setCharacterEncoding("UTF-8");

		    try {
		        response.getWriter().write("{\"error\": \"An internal server error occurred.\"}");
		    } catch (IOException ioException) {
		        ClientLogger.logError("IOException during response handling: " + ioException.getMessage());
		    }
		    
		} catch (IOException e1) {
			e1.printStackTrace();
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			
			ClientLogger.logWarning("Returning BAD REQUEST due to invalid or unprocessable request.");
			
			try {
				response.getWriter().write("{\"error\": \"The request was invalid or cannot be processed.\"}");
		    } catch (IOException ioException) {
		        ClientLogger.logError("IOException during response handling: " + ioException.getMessage());
		    }
		}
	}
	   
	private byte[] convertToByteArray(InputStream inputStream) throws IOException{
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();    
		byte[] buffer = new byte[1024];
		
		int bytesRead;
		while ((bytesRead = inputStream.read(buffer)) != -1)
			byteArrayOutputStream.write(buffer, 0, bytesRead);
		
		byte[] result = byteArrayOutputStream.toByteArray();
		byteArrayOutputStream.close();
		
		return result;
	}

	private String extractVehicleIdFromJson(String json) {
	    if (json.contains("veh")) {
	        int start = json.indexOf("veh") + 6; // Početak vrijednosti (ignoriranje "veh": ")
	        int end = json.indexOf('"', start); // Kraj vrijednosti
	        return json.substring(start, end); // Ekstrakcija ID-a
	    }
	    return null;
	}
}