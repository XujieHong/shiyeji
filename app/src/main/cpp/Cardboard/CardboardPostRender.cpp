/* Copyright (C) 2016 Tcl Corporation Limited */
#include<android/log.h>
#include <jni.h>
#include "vr_types.h"
#include "CardboardMesh.h"
#include "CardboardPostRender.h"

namespace android {

CardboardPostRender::CardboardPostRender(int width, int height)
		: mDistortionMesh(CardboardMesh::TRIANGLES, 2 * width * height, 2, 2) {
	mWidth = width;
	mHeight = height;
}

CardboardPostRender::~CardboardPostRender() {
}

void CardboardPostRender::rebuildDistortionMesh(float *buffer_vertices, float *buffer_tex, short *indices, float size) {
	CardboardMesh::VertexArray<POINT> vertices(mDistortionMesh.getPositionArray<POINT>());
	CardboardMesh::VertexArray<POINT> tex(mDistortionMesh.getTexCoordArray<POINT>());

	computeMeshPoints(mWidth, mHeight, vertices, tex);

    computeMeshIndices(mWidth, mHeight, indices);

	int i, j;
	for(i = 0; i < mHeight; i++)
	{
		for(j = 0; j < mWidth; j++)
		{
			buffer_vertices[i * 2 * mWidth + j * 2] = vertices[i * mWidth + j].x * size;
			buffer_vertices[i * 2 * mWidth + j * 2 + 1] = vertices[i * mWidth + j].y * size;

            buffer_tex[i * 2 * mWidth + j * 2] = tex[i * mWidth + j].x;
            buffer_tex[i * 2 * mWidth + j * 2 + 1] = tex[i * mWidth + j].y;
		}
	}
}

void CardboardPostRender::computeMeshPoints(int width, int height,
		CardboardMesh::VertexArray<POINT> vertices, CardboardMesh::VertexArray<POINT> tex) {

	float maxDistortEdge;
	mCardboardProfile.distortInv(1.0f, maxDistortEdge);
	int vidx = 0;
	for (int j = 0; j < height; j++) {
		for (int i = 0; i < width; i++, vidx++) {
			float s = (float) i / (width - 1);
			float t = (float) j / (height - 1);
			float u = (s * 2.0f - 1.0f) * maxDistortEdge;
			float v = (t * 2.0f - 1.0f) * maxDistortEdge;
			float d = sqrt(u * u + v * v);
			float r;
			mCardboardProfile.distort(d, r);
			u = u * r / d;
			v = v * r / d;

			vertices[vidx].x = u;
			vertices[vidx].y = -v;
			tex[vidx].x = s;
			tex[vidx].y = t;
		}
	}
}

void CardboardPostRender::computeMeshIndices(int width, int height, short *indices) {
	int iidx = 0, vidx = 0;

    for (int j = 0; j < height; j++) {
        for (int i = 0; i < width; i++, vidx++) {
            if (i == 0 || j == 0)
                continue;
            indices[iidx++] = vidx;
            indices[iidx++] = vidx - width;
            indices[iidx++] = vidx - width - 1;
            indices[iidx++] = vidx - width - 1;
            indices[iidx++] = vidx - 1;
            indices[iidx++] = vidx;
        }
    }
}

void CardboardPostRender::setDistortionParams(float k1, float k2, float k3) {
    mCardboardProfile.setDistortionParams(k1, k2, k3);
}

}
