# Pasteleria Mil Sabores
Integrantes: Tomás Ponce, Ulises Torres


Paso a paso de creación de la firma para la APK
↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
Para generar la firma necesaria para publicar la aplicación, primero abrimos Android Studio y fuimos a Build, Generate Signed App Bundle / APK. En la ventana que aparece seleccionamos Android App Bundle como formato de salida.

Como no teníamos una llave creada, elegimos la opción Create new. Definimos la ruta donde se guardaría el archivo .jks y establecimos una contraseña para el keystore. Luego configuramos la clave interna ingresando un alias, asignando la misma contraseña del keystore y definiendo un período de validez de 25 años, que es el tiempo durante el cual la firma será válida.

En la sección del certificado completamos únicamente los datos mínimos requeridos, específicamente el campo First and Last Name, para que la llave pudiera generarse correctamente. Finalmente, continuamos con el asistente y Android Studio generó el App Bundle ya firmado.


Imagen de archivo APK firmada dentro de la carpeta realease
↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
<img width="659" height="166" alt="image" src="https://github.com/user-attachments/assets/e4466aad-df90-476a-b4c7-22c66cc010d4" />

Captura de ejecución y creación de firma de la APK
↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
<img width="860" height="226" alt="image" src="https://github.com/user-attachments/assets/6cddb403-b291-43a0-bd60-493ac5946a72" />
