package com.ricoh.liferay.portal.demos.asturias.controller;

import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.service.JournalArticleLocalServiceUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

import java.io.IOException;
import java.util.List;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

/**
 * Portlet implementation class ReemplazoToponimos
 */
public class ReemplazoToponimos extends MVCPortlet {
	
	@Override
	public void init() throws PortletException {
		// TODO Auto-generated method stub
		System.out.println("Portlet Iniciado");
		
		super.init();
	}
	
	@Override
	public void doView(RenderRequest renderRequest,
			RenderResponse renderResponse) throws IOException, PortletException {
		// TODO Auto-generated method stub
		long result = -1;
		try {
		JournalArticle art = null;
		List<JournalArticle> articles = JournalArticleLocalServiceUtil
				.getArticles();
		for (JournalArticle article : articles) {
			String treePath = article.getTreePath();
			treePath.contains("/ciudades/");
			String rawtitle = article.getTitle();
			String rawcontent = article.getContent();
			String lenguajesDispo[]= article.getAvailableLanguageIds();
			String url= article.getUrlTitle();
			int beg = rawtitle.indexOf("<Title ");
			int begCont = rawcontent.indexOf("<div class=\"tab-content\" ");
			if (beg > 0) {
				beg = rawtitle.indexOf(">", beg) + 1;
				int end = rawtitle.indexOf("</Title", beg);
				int endCont=rawcontent.indexOf("</div", beg);
				String title = rawtitle.substring(beg, end);
				//String content = rawcontent.substring(begCont, endCont);
				System.out.println("Url: "+url);
				System.out.println("Titulo: "+title);
				System.out.println("Titulo Plano: "+rawtitle);
				//System.out.println("Contenido Plano: "+ rawcontent);
				
				
				Elements contents = Jsoup.parse(rawcontent).getElementsByTag("static-content");
				int size= contents.size();
				
				
				if(contents!=null && !contents.isEmpty()){
					for (Element element : contents) {
						CDataNode cdNOde = new CDataNode(element.data());
						Elements content=Jsoup.parse(cdNOde.getWholeText()).getElementsByClass("tab-content");
						if(content!=null && !content.isEmpty()){
							for (Element contentElements : content) {
								Elements elementos= contentElements.getAllElements();
								for (Element elemento : elementos) {
									if(elemento.ownText().contains("Gijón")){
										System.out.println("Elemento de contenido Original: "+ elemento.ownText());
										System.out.println("Elemento de contenido Corregido: "+elemento.ownText().replaceAll("Gijón", "Gijón / Xixón (capital)"));
									}
								}
							}	
						}
					}
				
				}
				
			}
		}
		if (art != null)
			result = art.getResourcePrimKey();
		} catch (Throwable t) {
			t.printStackTrace();
		}
		super.doView(renderRequest, renderResponse);
	}
	@Override
	public void doEdit(RenderRequest renderRequest,
			RenderResponse renderResponse) throws IOException, PortletException {
		// TODO Auto-generated method stub
		super.doEdit(renderRequest, renderResponse);
	}
 

}
