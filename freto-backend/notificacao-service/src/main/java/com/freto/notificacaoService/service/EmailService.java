package com.freto.notificacaoService.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.freto.notificacaoService.event.UserCreatedEvent;

import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EmailService {

  private final JavaMailSender mailSender;

  public void sendConfirmationEmail(UserCreatedEvent event) {
    try {
      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

      helper.setFrom("no-reply@doneres.dev");
      helper.setTo(event.email());
      helper.setSubject("Bem-vindo ao FRETO!");
      helper.setText(buildEmailHtml(event.name()), true);

      mailSender.send(message);

    } catch (Exception e) {
      throw new RuntimeException("Erro ao enviar email: " + e.getMessage(), e);
    }
  }

  private String buildEmailHtml(String name) {
    return """
        <!DOCTYPE html>
        <html lang="pt-BR">
        <head>
          <meta charset="UTF-8" />
          <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
          <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@400;500;600;700&display=swap" rel="stylesheet"/>
          <title>Bem-vindo ao FRETO!</title>
        </head>
        <body style="margin:0;padding:0;background-color:#F9FAFB;font-family:'Outfit',sans-serif;">
          <table width="100%%" cellpadding="0" cellspacing="0" style="background-color:#F9FAFB;padding:40px 16px;">
            <tr>
              <td align="center">
                <table width="100%%" cellpadding="0" cellspacing="0" style="max-width:600px;background:#ffffff;border-radius:16px;overflow:hidden;box-shadow:0 4px 24px rgba(0,0,0,0.08);">

                  <!-- HEADER -->
                  <tr>
                    <td style="background:linear-gradient(135deg,#7C3AED,#5B21B6);padding:36px 40px;text-align:center;">
                      <span style="font-family:'Outfit',sans-serif;font-size:32px;font-weight:700;color:#ffffff;letter-spacing:6px;text-transform:uppercase;">
                        FRETO
                      </span>
                      <p style="margin:8px 0 0;color:#EDE9FE;font-size:14px;letter-spacing:1px;">
                        Frete rápido, sem stress, sem surpresa.
                      </p>
                    </td>
                  </tr>

                  <!-- ÍCONE DE SUCESSO -->
                  <tr>
                    <td align="center" style="padding-top:40px;">
                      <table cellpadding="0" cellspacing="0">
                        <tr>
                          <td style="background:#22C55E;border-radius:50%%;width:64px;height:64px;text-align:center;vertical-align:middle;">
                            <span style="font-size:32px;color:#ffffff;">&#10003;</span>
                          </td>
                        </tr>
                      </table>
                    </td>
                  </tr>

                  <!-- CONTEÚDO -->
                  <tr>
                    <td style="padding:24px 40px 0;">
                      <h1 style="margin:0 0 8px;font-size:24px;font-weight:700;color:#111827;text-align:center;">
                        Olá, %s!
                      </h1>
                      <p style="margin:0 0 16px;font-size:16px;color:#22C55E;font-weight:600;text-align:center;">
                        Sua conta foi criada com sucesso!
                      </p>
                      <p style="margin:0 0 24px;font-size:15px;color:#6B7280;line-height:1.7;text-align:center;">
                        O <strong style="color:#111827;">FRETO</strong> conecta você aos melhores motoristas parceiros
                        para fretes e mudanças com praticidade, segurança e transparência.
                        Agora você já pode solicitar seu primeiro frete!
                      </p>
                    </td>
                  </tr>

                  <!-- CARD DE DESTAQUES -->
                  <tr>
                    <td style="padding:0 40px 32px;">
                      <table width="100%%" cellpadding="0" cellspacing="0" style="background:#F5F3FF;border-radius:12px;">
                        <tr>
                          <td style="padding:24px;">
                            <table width="100%%" cellpadding="0" cellspacing="0">
                              <tr>
                                <td style="padding:8px 0;">
                                  <span style="font-size:18px;">&#128667;</span>
                                  <span style="font-size:14px;color:#111827;margin-left:10px;font-weight:500;">Fretes rápidos e seguros</span>
                                </td>
                              </tr>
                              <tr>
                                <td style="padding:8px 0;">
                                  <span style="font-size:18px;">&#128205;</span>
                                  <span style="font-size:14px;color:#111827;margin-left:10px;font-weight:500;">Rastreamento em tempo real</span>
                                </td>
                              </tr>
                              <tr>
                                <td style="padding:8px 0;">
                                  <span style="font-size:18px;">&#128156;</span>
                                  <span style="font-size:14px;color:#111827;margin-left:10px;font-weight:500;">Motoristas verificados e confiáveis</span>
                                </td>
                              </tr>
                            </table>
                          </td>
                        </tr>
                      </table>
                    </td>
                  </tr>

                  <!-- BOTÃO CTA -->
                  <tr>
                    <td align="center" style="padding:0 40px 40px;">
                      <a href="http://localhost:3000"
                         style="display:inline-block;background:#7C3AED;color:#ffffff;font-family:'Outfit',sans-serif;font-size:16px;font-weight:600;text-decoration:none;padding:14px 40px;border-radius:9999px;letter-spacing:0.5px;">
                        Acessar minha conta &#8594;
                      </a>
                    </td>
                  </tr>

                  <!-- DIVISOR -->
                  <tr>
                    <td style="padding:0 40px;">
                      <hr style="border:none;border-top:1px solid #F3F4F6;margin:0;"/>
                    </td>
                  </tr>

                  <!-- RODAPÉ -->
                  <tr>
                    <td style="padding:24px 40px;text-align:center;">
                      <p style="margin:0 0 6px;font-size:13px;color:#6B7280;">
                        Você recebeu este e-mail porque criou uma conta na plataforma FRETO.
                      </p>
                      <p style="margin:0;font-size:13px;color:#6B7280;">
                        Se não foi você, ignore este e-mail.
                      </p>
                      <p style="margin:12px 0 0;font-size:12px;color:#9CA3AF;">
                        &#169; 2026 FRETO &middot; Todos os direitos reservados
                      </p>
                    </td>
                  </tr>

                </table>
              </td>
            </tr>
          </table>
        </body>
        </html>
        """
        .formatted(name);
  }
}