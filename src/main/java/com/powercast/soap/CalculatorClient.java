package com.powercast.soap;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;
import org.xml.sax.InputSource;

@Service
public class CalculatorClient {
    private final RestTemplate rest;

    public CalculatorClient(RestTemplate rest) {
        this.rest = rest;
    }

    public int add(int a, int b) throws Exception {
        String body =
            "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
              "<soap:Body>" +
                "<Add xmlns=\"http://tempuri.org/\">" +
                  "<intA>" + a + "</intA>" +
                  "<intB>" + b + "</intB>" +
                "</Add>" +
              "</soap:Body>" +
            "</soap:Envelope>";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_XML);
        HttpEntity<String> req = new HttpEntity<>(body, headers);

        String xmlResponse = rest.postForObject(
          "http://www.dneonline.com/calculator.asmx", req, String.class
        );

        // parse com XPath
        Document doc = DocumentBuilderFactory
                          .newInstance()
                          .newDocumentBuilder()
                          .parse(new InputSource(new StringReader(xmlResponse)));
        XPath xp = XPathFactory.newInstance().newXPath();
        String result = xp.evaluate(
          "//*[local-name()='AddResult']", doc
        );
        return Integer.parseInt(result);
    }
}
