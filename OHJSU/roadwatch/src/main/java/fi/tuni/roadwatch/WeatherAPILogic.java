package fi.tuni.roadwatch;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Class for handling the weather API calls and data.
 */
public class WeatherAPILogic {
    private double currentTemp;
    private double currentWind;
    private double currentCloud;
    private double tempAverage;
    private double tempMIN;
    private double tempMAX;

    private Date dateAndTime = Calendar.getInstance().getTime();
    private final ArrayList<WeatherData> weatherData = new ArrayList<>();
    private final ArrayList<WeatherDataMinMaxAvg> weatherAVGMinMax = new ArrayList<>();

    /**
     * Creates URL String based on given parameters to be used in creating API document
     * @param coordinates CoordinateConstraints of selected coordinates
     * @param startime Start time of wanted observation/forecast
     * @param endtime End time of wanted observation/forecast
     * @return String of wanted API url
     */
    public String createURLString(CoordinateConstraints coordinates, String startime, String endtime) throws ParseException {
        StringBuilder str = new StringBuilder();
        String latLonCoords = coordinates.minLat.toString() + ',' + coordinates.minLon.toString();

        if(timeAndDateAsDate(startime).after(dateAndTime)){
            str.append("https://opendata.fmi.fi/wfs?request=getFeature&version=2.0.0&storedquery_id=fmi::forecast::harmonie::surface::point::simple&latlon=").append(latLonCoords)
                    .append("&timestep=10&starttime=").append(startime).append("&endtime=").append(endtime).append("&parameters=temperature,windspeedms");
        }
        else{
            str.append("https://opendata.fmi.fi/wfs?request=getFeature&version=2.0.0&storedquery_id=fmi::observations::weather::simple&bbox=").append(coordinates.getAsString(','))
                    .append("&starttime=").append(startime).append("&endtime=").append(endtime).append("&timestep=120&parameters=t2m,ws_10min,n_man");
        }

        return str.toString();

    }

    /**
     * Creates URL String based on given parameters to be used in creating API document
     * @param coordinates CoordinateConstraints of selected coordinates
     * @param startime Start time of wanted observation/forecast
     * @param endtime End time of wanted observation/forecast
     * @return String of wanted API url
     */
    public String createAVGMINMAXurlString(CoordinateConstraints coordinates, String startime, String endtime){
        String str = "https://opendata.fmi.fi/wfs?request=getFeature&version=2.0.0&storedquery_id=fmi::observations::weather::hourly::simple&bbox=" + coordinates.getAsString(',') +
                "&starttime=" + startime + "&endtime=" + endtime + "&parameters=TA_PT1H_AVG,TA_PT1H_MAX,TA_PT1H_MIN";
        return str;
    }

    /**
     * Read of AVG MIN MAX API and saving into ArrayList containing WeatherDataMinMaxAvg objects
     * @param doc wanted API document
     * @return ArrayList containing WeatherDataMinMaxAvg objects
     */
    public ArrayList<WeatherDataMinMaxAvg> creatingAvgMinMax(Document doc) throws ParseException {
        NodeList nList = doc.getElementsByTagName("wfs:member");
        int counter = 0;
        for (int temp = 0; temp < nList.getLength(); temp++) {
            counter++;
            Node nNode = nList.item(temp);
            Element eElement = (Element) nNode;
            String currentTime = eElement.getElementsByTagName("BsWfs:Time")
                    .item(0).getTextContent();
            Date currentDate = timeAndDateAsDate(currentTime);

            String currentCoordinates = eElement.getElementsByTagName("gml:pos")
                    .item(0).getTextContent();

            if(nNode.getNodeType() == Node.ELEMENT_NODE){
                if (currentTime.equals(eElement.getElementsByTagName("BsWfs:Time")
                        .item(0).getTextContent()) ){

                    String paramName = eElement.getElementsByTagName("BsWfs:ParameterName")
                            .item(0).getTextContent();
                    if( paramName.equals("TA_PT1H_AVG") ){
                        this.tempAverage = Double.parseDouble(eElement.getElementsByTagName("BsWfs:ParameterValue")
                                .item(0).getTextContent());
                    }
                    if( paramName.equals("TA_PT1H_MIN") ){
                        this.tempMIN = Double.parseDouble(eElement.getElementsByTagName("BsWfs:ParameterValue")
                                .item(0).getTextContent());
                    }
                    if( paramName.equals("TA_PT1H_MAX") ){
                        this.tempMAX = Double.parseDouble(eElement.getElementsByTagName("BsWfs:ParameterValue")
                                .item(0).getTextContent());
                    }
                    // Saves all weatherdata members to arraylist of weatherdata
                    if(counter % 3 == 0){
                        WeatherDataMinMaxAvg savetemp = new WeatherDataMinMaxAvg(currentDate , currentCoordinates, tempAverage, tempMIN, tempMAX);
                        if(!weatherAVGMinMax.contains(savetemp)){
                            weatherAVGMinMax.add(savetemp);
                        }
                    }
                }
            }
        }
        return weatherAVGMinMax;
    }

    /**
     * Creates Document element based on given url. Used in creatingWeatherData
     * @param url of the wanted API
     * @return Document object of wanted urlstring
     */
    public Document GetApiDocument(String url) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(new URL(url).openStream());
        doc.getDocumentElement().normalize();

        return doc;
    }

    /**
     * Creates observations weather data. Has to be different function due to different parameter names versus forecast
     * @param doc wanted API document
     * @return ArrayList containing WeatherData objects
     */
    public ArrayList<WeatherData> creatingWeatherObservations(Document doc) throws ParseException {
        NodeList nList = doc.getElementsByTagName("wfs:member");
        int counter = 0;
        for (int temp = 0; temp < nList.getLength(); temp++) {
            counter ++;
            Node nNode = nList.item(temp);
            Element eElement = (Element) nNode;
            String currentTime = eElement.getElementsByTagName("BsWfs:Time")
                    .item(0).getTextContent();
            Date currentDate = timeAndDateAsDate(currentTime);

            String currentCoordinates = eElement.getElementsByTagName("gml:pos")
                    .item(0).getTextContent();

            if(nNode.getNodeType() == Node.ELEMENT_NODE){
                if (currentTime.equals(eElement.getElementsByTagName("BsWfs:Time")
                        .item(0).getTextContent()) ){

                    String paramName = eElement.getElementsByTagName("BsWfs:ParameterName")
                            .item(0).getTextContent();
                    if( paramName.equals("t2m") ){
                        this.currentTemp = Double.parseDouble(eElement.getElementsByTagName("BsWfs:ParameterValue")
                                .item(0).getTextContent());
                    }
                    if( paramName.equals("ws_10min") ){
                        this.currentWind = Double.parseDouble(eElement.getElementsByTagName("BsWfs:ParameterValue")
                                .item(0).getTextContent());
                    }
                    if( paramName.equals("n_man") ){
                        this.currentCloud = Double.parseDouble(eElement.getElementsByTagName("BsWfs:ParameterValue")
                                .item(0).getTextContent());
                    }

                    // Saves all weatherdata members to arraylist of weatherdata
                    if(counter % 3 == 0){
                        WeatherData savetemp = new WeatherData(currentTemp, currentWind, currentCloud,currentDate , currentCoordinates);
                        if(!weatherData.contains(savetemp)){
                            weatherData.add(savetemp);
                        }
                    }

                }

            }
        }
        return weatherData;
    }

    /**
     * Creates forecast weather data. Has to be different function due to different parameter names versus observations
     * @param doc wanted API document
     * @return ArrayList containing WeatherData objects
     */
    public ArrayList<WeatherData> creatingWeatherForecast(Document doc) throws ParseException {
        NodeList nList = doc.getElementsByTagName("wfs:member");
        int counter = 0;
        for (int temp = 0; temp < nList.getLength(); temp++) {
            counter ++;
            Node nNode = nList.item(temp);
            Element eElement = (Element) nNode;
            String currentTime = eElement.getElementsByTagName("BsWfs:Time")
                    .item(0).getTextContent();
            Date currentDate = timeAndDateAsDate(currentTime);

            String currentCoordinates = eElement.getElementsByTagName("gml:pos")
                    .item(0).getTextContent();

            if(nNode.getNodeType() == Node.ELEMENT_NODE){
                if (currentTime.equals(eElement.getElementsByTagName("BsWfs:Time")
                        .item(0).getTextContent()) ){

                    String paramName = eElement.getElementsByTagName("BsWfs:ParameterName")
                            .item(0).getTextContent();
                    if( paramName.equals("temperature") ){
                        this.currentTemp = Double.parseDouble(eElement.getElementsByTagName("BsWfs:ParameterValue")
                                .item(0).getTextContent());
                    }
                    if( paramName.equals("windspeedms") ){
                        this.currentWind = Double.parseDouble(eElement.getElementsByTagName("BsWfs:ParameterValue")
                                .item(0).getTextContent());
                    }

                    if(counter % 2 == 0){
                        WeatherData savetemp = new WeatherData(currentTemp, currentWind, currentCloud,currentDate , currentCoordinates);
                        if(!weatherData.contains(savetemp)){
                            weatherData.add(savetemp);
                        }
                    }

                }

            }
        }
        return weatherData;
    }

    /**
     * Transforms string in ISO8601 format to Date object
     * @param datestring string in ISO8601 format
     * @return Date object of datestring
     */
    public Date timeAndDateAsDate(String datestring) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(datestring);
    }
}
