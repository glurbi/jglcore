#version 330 core

uniform mat4 mvpMatrix;
uniform vec3 color;

in vec3 vPosition;
in vec3 vNormal;
out vec4 vColor;

void main(void) 
{ 
	gl_Position = mvpMatrix * vec4(vPosition, 1.0);
	vColor = vec4(color, 1.0f);
}
