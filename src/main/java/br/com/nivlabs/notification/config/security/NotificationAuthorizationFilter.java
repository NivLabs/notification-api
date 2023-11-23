package br.com.nivlabs.notification.config.security;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import br.com.nivlabs.notification.domain.Channel;
import br.com.nivlabs.notification.exception.HttpException;
import br.com.nivlabs.notification.repository.ChannelRepository;
import br.com.nivlabs.notification.security.UserDetailsCustom;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class NotificationAuthorizationFilter extends BasicAuthenticationFilter {

    private ChannelRepository channelRepo;

    public NotificationAuthorizationFilter(AuthenticationManager authenticationManager, ChannelRepository channelRepo) {
        super(authenticationManager);
        this.channelRepo = channelRepo;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse resp, FilterChain filterChain)
            throws IOException, ServletException {

        String channelUUID = req.getHeader("CHANNEL_UUID");

        if (channelUUID != null) {

            UsernamePasswordAuthenticationToken auth = null;
            try {
                auth = getAuthentication(channelUUID);
            } catch (HttpException e) {
                if (e.getStatus() == HttpStatus.UNAUTHORIZED) {
                    resp.setStatus(401);
                    resp.setContentType("application/json;charset=utf-8");
                    resp.getWriter().append(json(req, e));
                    return;
                }
            }

            if (auth != null)
                SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(req, resp);
    }

    /**
     * Converte os erros tratador Http da camada de utilidades de Token para objetos de resposta do filtro
     * 
     * @param req Requisição http
     * @param e Exception lançada pelo utilitário
     * @return Json em String para setar no Stream da resposta do filtro
     */
    private String json(HttpServletRequest req, HttpException e) {
        String jsonResponse = """
                {
                   "timestamp": DATE,
                   "status": 401,
                   "error": "ERROR",
                   "message": "MESSAGE",
                   "path": "PATH"
                }
                """;
        return ("" + jsonResponse)
                .replace("DATE", LocalDateTime.now().toString())
                .replace("ERROR", "Não autorizado")
                .replace("MESSAGE", "Não autorizado")
                .replace("PATH", req.getServletPath());
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String channelUuid) {
        if (channelUuid != null) {
            final Channel channel = channelRepo.findById(channelUuid)
                    .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Channel not found"));

            final var userOfSystem = new UserDetailsCustom(channelUuid, channel.getName(), false);

            return new UsernamePasswordAuthenticationToken(userOfSystem, null, userOfSystem.getAuthorities());
        }
        return null;
    }

}
