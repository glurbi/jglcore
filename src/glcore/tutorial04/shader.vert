#version 400

uniform mat4 mvpMatrix;

in vec3 position;
in vec3 color;
out vec4 gl_Color;

void main(void) 
{ 
	gl_Position = mvpMatrix * vec4(position, 1.0f);
	gl_Color = vec4(color, 1.0f);
}
