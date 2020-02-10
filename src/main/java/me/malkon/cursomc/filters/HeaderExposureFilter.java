package me.malkon.cursomc.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

/*Toda vez q for feita uma requisicao este filtro vai interceptar, add o cabeçalho location
 * na resposta e depois seguir com ciclo normal da requisicao*/
@Component
public class HeaderExposureFilter implements Filter {

	@Override // fazer algo qnd o filtro for criado
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		/*
		 * cabeçalho tem q ficar exposto p o angular pode localizar a uri de retorno qnd
		 * um recurso é criado.
		 */
		HttpServletResponse res = (HttpServletResponse) response;
		res.addHeader("access-control-expose-headers", "location");// libera o acesso ao cabeçalho location
		chain.doFilter(request, response);
	}

	@Override // fazer algo qnd o filtro for destruido
	public void destroy() {
	}
}
