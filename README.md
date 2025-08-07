# Sistema de Reciclaje Inteligente con Spring Boot y Thymeleaf ♻️

Este proyecto es una aplicación web de reciclaje inteligente que permite a los usuarios registrar sus actividades de reciclaje y ganar beneficios por ello. Además, cuenta con autenticación basada en roles (ADMIN, USER) y funcionalidades adicionales como un chatbot, historial de reciclaje, gestión de estaciones, recompensas y beneficios.

## Tecnologías utilizadas

- Java 17
- Spring Boot
- Spring Security + JWT
- Thymeleaf (frontend)
- JPA + Hibernate
- PosgreSQL
- Bootstrap + HTML/CSS
- Spring web
- Maven

## Funcionalidades principales

### Para usuarios:
- Registro e inicio de sesión
- Panel de usuario con resumen
- Registro de reciclajes (plástico, papel, etc.)
- Generación de historial por usuario
- Acumulación de puntos/kg reciclados
- Visualización y reclamo de beneficios (desde los 100kg)
- Generación de tickets de descuento
- Chatbot informativo sobre reciclaje
- Acceso a contenido de educación ambiental

### Para administradores:
- Gestión de usuarios (listar, buscar por nombre o email)
- Gestión de estaciones de reciclaje
- Gestión de recompensas
- Vista general del sistema y reportes

## Seguridad

- Inicio de sesión con roles diferenciados
- Seguridad configurada con `SecurityFilterChain`
- Cifrado de contraseñas con BCryptPasswordEncoder
- Validación de rutas protegidas por rol (`hasRole`, `hasAnyRole`)
- Redirección personalizada tras login exitoso

## Estructura del proyecto

```
Sistema-Reciclaje-Inteligente-Boot/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/reciclaje/
│   │   │       ├── controllers/
│   │   │       ├── entities/
│   │   │       ├── repositories/
│   │   │       ├── services/
│   │   │       └── config/
│   │   └── resources/
│   │       ├── templates/
│   │       └── static/
├── pom.xml
└── README.md
```

## Cómo ejecutar el proyecto

1. Clonar el repositorio:
```bash
git clone https://github.com/tu-usuario/Sistema-Reciclaje-Inteligente-Boot.git
```

2. Configurar la base de datos en `application.properties`.

3. Ejecutar el proyecto con:
```bash
mvn spring-boot:run
```

4. Accede a git remote add origin https://github.com/ChristianHuarcaya/Sistema-reciclaje-inteligente.git


## 👨‍💻 Autor

**Cristian Huarcaya Pumahualcca**  
Desarrollador Backend en Java  
[LinkedIn](https://www.linkedin.com/in/christian-huarcaya-pumahualcca) | [GitHub](https://github.com/ChristianHuarcaya)




