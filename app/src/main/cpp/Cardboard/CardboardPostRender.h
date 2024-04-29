/* Copyright (C) 2016 Tcl Corporation Limited */
#ifndef ANDROID_CARDBOARDPOSTRENDER_H
#define ANDROID_CARDBOARDPOSTRENDER_H

#include "vr_types.h"
#include "CardboardProfile.h"
#include "CardboardMesh.h"

typedef struct Point {
    float x;
    float y;
}POINT;


namespace android {

class CardboardPostRender {

public:
	CardboardPostRender(int width, int height);
	~CardboardPostRender();
    void rebuildDistortionMesh(float *buffer_vertices, float *buffer_tex, short *indices, float size);

    void setDistortionParams(float k1, float k2, float k3);

private:
	int mWidth;
	int mHeight;



	void computeMeshPoints(int width, int height,
						   CardboardMesh::VertexArray<POINT> vertices, CardboardMesh::VertexArray<POINT> tex);
	void computeMeshIndices(int width, int height, short *indices);
	CardboardMesh mDistortionMesh;
	CardboardProfile mCardboardProfile;
};
}
#endif
