import React, { useRef, useState } from 'react'
import { useGLTF } from '@react-three/drei'
import { useFrame } from '@react-three/fiber';

export default function Model(props) {
  const { nodes, materials, scene } = useGLTF('/static/earth.gltf');
  
  const earthRef = useRef(null);
  const [isInteracting, setInteracting] = useState(false);
  
  useFrame(() => {
    if (!isInteracting && earthRef.current) {
        earthRef.current.rotation.y += 0.01;
    }
});
  return (
    <group {...props} dispose={null}>
      <mesh geometry={nodes.Object_4.geometry} material={materials['Scene_-_Root']} scale={1.128} />
      <mesh
          ref={earthRef}
          onPointerOver={() => setInteracting(true)}
          onPointerOut={() => setInteracting(false)}
          scale={[2.3, 2.3, 2.3]}
        >
          <primitive object={scene} />
      </mesh>
    </group>
  );
}

useGLTF.preload('/static/earth.gltf')
