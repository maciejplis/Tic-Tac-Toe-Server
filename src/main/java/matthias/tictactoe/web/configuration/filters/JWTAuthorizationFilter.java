package matthias.tictactoe.web.configuration.filters;

import io.jsonwebtoken.*;
import matthias.tictactoe.web.configuration.JWTConfig;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class JWTAuthorizationFilter extends OncePerRequestFilter {
    private final JWTConfig jwtConfig = JWTConfig.getConfig();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader(jwtConfig.getHeader());

        if(authHeader != null && authHeader.startsWith(jwtConfig.getPrefix())) {
            try {
                Claims claims = validateToken(authHeader);
                setUpSpringAuthentication(claims);
            } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException e) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
                return;
            }
        } else {
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }

    private Claims validateToken(String token) {
        token = token.replace(jwtConfig.getPrefix(), "");

        return Jwts.parser()
                .setSigningKey(jwtConfig.getSecretKey().getBytes())
                .parseClaimsJws(token)
                .getBody();
    }

    private void setUpSpringAuthentication(Claims claims) {
        @SuppressWarnings("unchecked")
        List<String> authorities = (List<String>)claims.get("authorities");

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(claims.getSubject(), null,
                authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        SecurityContextHolder.getContext().setAuthentication(auth);
    }


}
